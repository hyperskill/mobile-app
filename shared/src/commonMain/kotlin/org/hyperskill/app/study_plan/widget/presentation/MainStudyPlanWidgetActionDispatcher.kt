package org.hyperskill.app.study_plan.widget.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.profile.domain.model.isMobileContentTrialEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalMessage
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.subscriptions.domain.model.orContentTrial
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainStudyPlanWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val learningActivitiesRepository: LearningActivitiesRepository,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val progressesRepository: ProgressesRepository,
    private val purchaseInteractor: PurchaseInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentProfileStateRepository.changes
            .distinctUntilChanged()
            .onEach { profile ->
                onNewMessage(InternalMessage.ProfileChanged(profile))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchLearningActivitiesWithSections -> {
                handleFetchLearningActivitiesWithSectionsAction(action, ::onNewMessage)
            }
            is InternalAction.FetchLearningActivities -> {
                handleFetchLearningActivitiesAction(action, ::onNewMessage)
            }
            is InternalAction.FetchProfile -> {
                sentryInteractor.withTransaction(
                    HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchProfile(),
                    onError = { StudyPlanWidgetFeature.ProfileFetchResult.Failed }
                ) {
                    currentProfileStateRepository
                        .getState()
                        .getOrThrow()
                        .let(StudyPlanWidgetFeature.ProfileFetchResult::Success)
                }.let(::onNewMessage)
            }
            is InternalAction.UpdateNextLearningActivityState -> {
                nextLearningActivityStateRepository.updateState(newState = action.learningActivity)
            }
            is InternalAction.UpdateCurrentStudyPlanState -> {
                currentStudyPlanStateRepository.getState(forceUpdate = action.forceUpdate)
            }
            is InternalAction.PutTopicsProgressesToCache -> {
                progressesRepository.putTopicsProgressesToCache(action.topicsProgresses)
            }
            is InternalAction.CaptureSentryException -> {
                sentryInteractor.captureException(action.throwable)
            }
            is InternalAction.FetchPaymentAbility -> {
                purchaseInteractor
                    .canMakePayments()
                    .getOrDefault(false)
                    .let {
                        onNewMessage(InternalMessage.FetchPaymentAbilityResult(it))
                    }
            }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchLearningActivitiesWithSectionsAction(
        action: InternalAction.FetchLearningActivitiesWithSections,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivitiesWithSections(),
            onError = { StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Failed }
        ) {
            coroutineScope {
                val learningActivitiesDeferred = async {
                    learningActivitiesRepository
                        .getLearningActivitiesWithSections(
                            studyPlanSectionTypes = action.studyPlanSectionTypes,
                            learningActivityTypes = action.learningActivityTypes,
                            learningActivityStates = action.learningActivityStates
                        )
                }
                val subscriptionDeferred = async {
                    currentSubscriptionStateRepository.getState()
                }

                val profile = currentProfileStateRepository.getState().getOrNull()

                val trackProgressDeferred = async {
                    if (profile?.trackId != null) {
                        progressesRepository.getTrackProgress(
                            trackId = profile.trackId,
                            forceLoadFromRemote = true
                        )
                    } else {
                        Result.success(null)
                    }
                }

                val canMakePayments = purchaseInteractor.canMakePayments().getOrDefault(false)

                val learningActivitiesResponse = learningActivitiesDeferred.await().getOrThrow()
                val subscription =
                    subscriptionDeferred
                        .await()
                        .getOrThrow()
                        .orContentTrial(
                            isMobileContentTrialEnabled = profile?.features?.isMobileContentTrialEnabled == true,
                            canMakePayments = canMakePayments
                        )
                val trackProgress = trackProgressDeferred.await().getOrThrow()

                StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success(
                    learningActivities = learningActivitiesResponse.learningActivities,
                    studyPlanSections = learningActivitiesResponse.studyPlanSections,
                    subscription = subscription,
                    learnedTopicsCount = trackProgress?.learnedTopicsCount ?: 0,
                    canMakePayments = canMakePayments
                )
            }
        }.let(onNewMessage)
    }

    private suspend fun handleFetchLearningActivitiesAction(
        action: InternalAction.FetchLearningActivities,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            action.sentryTransaction,
            onError = { StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed(action.sectionId) }
        ) {
            learningActivitiesRepository
                .getLearningActivities(
                    activitiesIds = action.activitiesIds,
                    types = action.types,
                    states = action.states
                )
                .getOrThrow()
                .let { learningActivities ->
                    StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                        sectionId = action.sectionId,
                        activities = learningActivities
                    )
                }
        }.let(onNewMessage)
    }
}