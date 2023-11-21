package org.hyperskill.app.study_plan.widget.presentation

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.learning_activities.domain.repository.NextLearningActivityStateRepository
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalMessage
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val studyPlanInteractor: StudyPlanInteractor,
    private val nextLearningActivityStateRepository: NextLearningActivityStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentProfileStateRepository.changes
            .distinctUntilChanged()
            .onEach(InternalMessage::ProfileChanged)
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchStudyPlan -> {
                if (action.delayBeforeFetching != null) {
                    delay(action.delayBeforeFetching)
                }

                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchCurrentStudyPlan()
                sentryInteractor.startTransaction(sentryTransaction)

                studyPlanInteractor.getCurrentStudyPlan(forceLoadFromRemote = true)
                    .onSuccess { studyPlan ->
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(
                            StudyPlanWidgetFeature.StudyPlanFetchResult.Success(
                                studyPlan = studyPlan,
                                attemptNumber = action.attemptNumber,
                                showLoadingIndicators = action.showLoadingIndicators
                            )
                        )
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.StudyPlanFetchResult.Failed)
                    }
            }
            is InternalAction.FetchSections -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchStudyPlanSections()
                sentryInteractor.startTransaction(sentryTransaction)

                studyPlanInteractor.getStudyPlanSections(action.sectionsIds)
                    .onSuccess { sections ->
                        sentryInteractor.finishTransaction(sentryTransaction)
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Success(sections))
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Failed)
                    }
            }
            is InternalAction.FetchActivities -> {
                sentryInteractor.startTransaction(action.sentryTransaction)

                studyPlanInteractor.getLearningActivities(action.activitiesIds, action.types, action.states)
                    .onSuccess { learningActivities ->
                        sentryInteractor.finishTransaction(action.sentryTransaction)
                        onNewMessage(
                            StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                                action.sectionId,
                                learningActivities
                            )
                        )
                    }
                    .onFailure {
                        sentryInteractor.finishTransaction(action.sentryTransaction, throwable = it)
                        onNewMessage(StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed(action.sectionId))
                    }
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
            is InternalAction.CaptureSentryException -> {
                sentryInteractor.captureException(action.throwable)
            }
            is InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }
}