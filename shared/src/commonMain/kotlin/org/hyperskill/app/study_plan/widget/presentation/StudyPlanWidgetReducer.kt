package org.hyperskill.app.study_plan.widget.presentation

import kotlin.math.min
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.core.utils.indexOfFirstOrNull
import org.hyperskill.app.core.utils.mutate
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedActivityHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSectionHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSubscribeHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanExpandCompletedActivitiesClickedHSAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanLoadMoreActivitiesClickedHSAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.domain.model.firstRootTopicsActivityIndexToBeLoaded
import org.hyperskill.app.study_plan.widget.domain.mapper.LearningActivityToTopicProgressMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalMessage
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.State
import ru.nobird.app.core.model.slice
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanWidgetReducerResult = Pair<State, Set<Action>>

class StudyPlanWidgetReducer : StateReducer<State, Message, Action> {
    companion object {
        const val SECTION_ROOT_TOPICS_PAGE_SIZE = 10
    }

    override fun reduce(state: State, message: Message): StudyPlanWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize ->
                coldContentFetch(state, message)
            is StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success ->
                handleLearningActivitiesWithSectionsFetchSuccess(state, message)
            StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.ERROR) to emptySet()
            }
            is Message.RetryActivitiesLoading ->
                handleRetryActivitiesLoading(state, message)
            is Message.LoadMoreActivitiesClicked ->
                handleLoadMoreActivitiesClicked(state, message)
            is InternalMessage.ReloadContentInBackground -> {
                val currentSectionId = state.getCurrentSection()?.id
                state.copy(
                    studyPlanSections = state.studyPlanSections.mapValues { (sectionId, sectionInfo) ->
                        sectionInfo.copy(
                            sectionContentStatus = if (sectionId == currentSectionId) {
                                sectionInfo.sectionContentStatus
                            } else {
                                SectionContentStatus.IDLE
                            }
                        )
                    }
                ) to getContentFetchActions(forceUpdate = true)
            }
            is Message.ExpandCompletedActivitiesClicked ->
                handleExpandCompletedActivitiesClicked(state, message)
            is Message.PullToRefresh ->
                if (!state.isRefreshing) {
                    state.copy(isRefreshing = true) to getContentFetchActions(forceUpdate = true)
                } else {
                    null
                }
            is Message.SubscribeClicked -> handleSubscribeClicked(state)
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success ->
                handleLearningActivitiesFetchSuccess(state, message)
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed ->
                handleLearningActivitiesFetchFailed(state, message)
            is StudyPlanWidgetFeature.ProfileFetchResult.Success -> {
                state.copy(profile = message.profile) to emptySet()
            }
            is StudyPlanWidgetFeature.ProfileFetchResult.Failed -> {
                null
            }
            is InternalMessage.ProfileChanged -> {
                state.copy(profile = message.profile) to emptySet()
            }
            is Message.SectionClicked ->
                changeSectionExpanse(state, message.sectionId, shouldLogAnalyticEvent = true)
            is Message.ActivityClicked ->
                handleActivityClicked(state, message)
            is Message.StageImplementUnsupportedModalGoToHomeClicked ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.StudyPlan()
                        )
                    ),
                    Action.ViewAction.NavigateTo.Home
                )
            is Message.StageImplementUnsupportedModalHiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.StudyPlan()
                        )
                    )
                )
            is Message.StageImplementUnsupportedModalShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent(
                            route = HyperskillAnalyticRoute.StudyPlan()
                        )
                    )
                )
            is InternalMessage.FetchSubscriptionLimitTypeResult ->
                handleFetchSubscriptionLimitTypeResult(state, message)
            is InternalMessage.SubscriptionLimitTypeChanged ->
                handleSubscriptionLimitTypeChanged(state, message)
        } ?: (state to emptySet())

    private fun coldContentFetch(state: State, message: InternalMessage.Initialize): StudyPlanWidgetReducerResult =
        if (state.sectionsStatus == StudyPlanWidgetFeature.ContentStatus.IDLE ||
            state.sectionsStatus == StudyPlanWidgetFeature.ContentStatus.ERROR && message.forceUpdate
        ) {
            State(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING) to
                getContentFetchActions(forceUpdate = message.forceUpdate)
        } else {
            state to emptySet()
        }

    private fun getContentFetchActions(forceUpdate: Boolean): Set<Action> =
        setOfNotNull(
            InternalAction.FetchLearningActivitiesWithSections(),
            InternalAction.FetchProfile,
            InternalAction.UpdateCurrentStudyPlanState(forceUpdate)
        )

    private fun handleLearningActivitiesWithSectionsFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val learningActivitiesIds = message.learningActivities.map { it.id }.toSet()
        val visibleSections = getVisibleSections(message.studyPlanSections, learningActivitiesIds)
        val currentSectionId = visibleSections.firstOrNull()?.id ?: return state.copy(
            studyPlanSections = emptyMap(),
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isRefreshing = false,
            subscriptionLimitType = message.subscriptionLimitType
        ) to emptySet()

        val supportedSections = visibleSections
            .filter { studyPlanSection ->
                // ALTAPPS-1186: We should hide next project section for freemium users
                if (!message.subscription.type.isProjectSelectionEnabled) {
                    studyPlanSection.type != StudyPlanSectionType.NEXT_PROJECT
                } else {
                    true
                }
            }

        val studyPlanSections = supportedSections.associate { studyPlanSection ->
            studyPlanSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = studyPlanSection.id == currentSectionId,
                sectionContentStatus = if (studyPlanSection.id == currentSectionId) {
                    SectionContentStatus.FIRST_PAGE_LOADED
                } else {
                    SectionContentStatus.IDLE
                }
            )
        }

        val loadedSectionsState = state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isRefreshing = false,
            learnedTopicsCount = message.learnedTopicsCount,
            activities = message.learningActivities.associateBy { it.id },
            subscriptionLimitType = message.subscriptionLimitType
        )

        return if (loadedSectionsState.studyPlanSections.isNotEmpty()) {
            handleNewActivities(
                loadedSectionsState,
                sectionId = currentSectionId,
                activities = message.learningActivities
            )
        } else {
            loadedSectionsState to emptySet()
        }
    }

    private fun getVisibleSections(
        sections: List<StudyPlanSection>,
        learningActivitiesIds: Set<Long>
    ): List<StudyPlanSection> {
        /**
         * The current section is the section that contains learning activities that were returned.
         * There could be a situation when the API returns activities from not first visible section.
         *
         * So, we should hide all visible sections that above of current section.
         * For example, the first visible section contains only not supported activities.
         */
        val visibleSections = sections.filter { it.isVisible }
        val currentSectionIndex = visibleSections
            .indexOfFirstOrNull { studyPlanSection ->
                studyPlanSection.activities.intersect(learningActivitiesIds).isNotEmpty()
            }
            ?: return emptyList()
        return visibleSections.slice(from = currentSectionIndex)
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success,
    ): StudyPlanWidgetReducerResult =
        handleNewActivities(
            state = state.copy(
                activities = state.activities.mutate {
                    putAll(message.activities.associateBy { it.id })

                    val isRootTopicsSection =
                        state
                            .studyPlanSections[message.sectionId]
                            ?.studyPlanSection
                            ?.type == StudyPlanSectionType.ROOT_TOPICS
                    if (!isRootTopicsSection) {
                        // ALTAPPS-743: We should remove activities that are not in the new list
                        // (e.g. when user has completed or skipped some activities)
                        val activitiesIds =
                            state.studyPlanSections[message.sectionId]?.studyPlanSection?.activities?.toSet()
                                ?: emptySet()
                        val activitiesIdsToRemove = activitiesIds - message.activities.map { it.id }.toSet()
                        activitiesIdsToRemove.forEach { remove(it) }
                    }
                }
            ),
            sectionId = message.sectionId,
            activities = message.activities
        )

    private fun handleNewActivities(
        state: State,
        sectionId: Long,
        activities: List<LearningActivity>
    ): StudyPlanWidgetReducerResult {
        val sectionContentStatus = state.studyPlanSections[sectionId]?.sectionContentStatus
        val nextState =
            when {
                // ALTAPPS-786: We should hide sections without available activities to avoid blocking study plan
                activities.isEmpty() && sectionContentStatus == SectionContentStatus.FIRST_PAGE_LOADING -> {
                    state.copy(
                        studyPlanSections = state.studyPlanSections.mutate {
                            remove(sectionId)
                        }
                    )
                }
                else -> {
                    state.copy(
                        studyPlanSections = state.studyPlanSections.update(sectionId) { sectionInfo ->
                            sectionInfo.copy(
                                sectionContentStatus = when (sectionContentStatus) {
                                    SectionContentStatus.FIRST_PAGE_LOADED -> {
                                        val canLoadMoreActivities =
                                            state
                                                .getNextRootTopicsActivitiesToBeLoaded(sectionId)
                                                .isNotEmpty()
                                        if (canLoadMoreActivities) {
                                            SectionContentStatus.FIRST_PAGE_LOADED
                                        } else {
                                            SectionContentStatus.ALL_PAGES_LOADED
                                        }
                                    }
                                    else -> SectionContentStatus.ALL_PAGES_LOADED
                                }
                            )
                        }
                    )
                }
            }

        val isFetchedActivitiesForCurrentSection = sectionId == state.getCurrentSection()?.id

        // ALTAPPS-786: We should expand next section if current section doesn't have available activities
        val (resultState, resultActions) =
            if (isFetchedActivitiesForCurrentSection && activities.isEmpty()) {
                nextState.studyPlanSections.keys.firstOrNull()?.let { nextSectionId ->
                    changeSectionExpanse(nextState, nextSectionId, shouldLogAnalyticEvent = false)
                } ?: (nextState to emptySet())
            } else {
                nextState to emptySet()
            }

        val updateNextLearningActivityStateAction = if (isFetchedActivitiesForCurrentSection) {
            InternalAction.UpdateNextLearningActivityState(resultState.getCurrentActivity())
        } else {
            null
        }

        return resultState to (
            resultActions +
                setOfNotNull(updateNextLearningActivityStateAction) +
                setOf(putFetchedLearningActivitiesProgressesToCacheAction(activities))
            )
    }

    private fun putFetchedLearningActivitiesProgressesToCacheAction(
        activities: List<LearningActivity>
    ): Action {
        val progresses = activities.mapNotNull(LearningActivityToTopicProgressMapper::map)
        return InternalAction.PutTopicsProgressesToCache(progresses)
    }

    private fun handleLearningActivitiesFetchFailed(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed
    ): StudyPlanWidgetReducerResult =
        state.copy(
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(
                    sectionContentStatus = when (sectionInfo.sectionContentStatus) {
                        SectionContentStatus.IDLE,
                        SectionContentStatus.ERROR,
                        SectionContentStatus.FIRST_PAGE_LOADING -> SectionContentStatus.ERROR
                        SectionContentStatus.NEXT_PAGE_LOADING,
                        SectionContentStatus.FIRST_PAGE_LOADED,
                        SectionContentStatus.ALL_PAGES_LOADED -> SectionContentStatus.FIRST_PAGE_LOADED
                    }
                )
            }
        ) to emptySet()

    private fun handleRetryActivitiesLoading(
        state: State,
        message: Message.RetryActivitiesLoading
    ): StudyPlanWidgetReducerResult {
        val section =
            state.studyPlanSections[message.sectionId] ?: return state to emptySet()
        return state.copy(
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(sectionContentStatus = SectionContentStatus.FIRST_PAGE_LOADING)
            }
        ) to setOf(
            InternalAction.FetchLearningActivities(
                sectionId = message.sectionId,
                activitiesIds = getPaginatedActivitiesIds(
                    section = section,
                    isLearningPathDividedTrackTopicsEnabled = state.isLearningPathDividedTrackTopicsEnabled
                ),
                sentryTransaction = getFetchLearningActivitiesSentryTransaction(state, message.sectionId)
            ),
            InternalAction.LogAnalyticEvent(
                StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent(message.sectionId)
            )
        )
    }

    private fun handleLoadMoreActivitiesClicked(
        state: State,
        message: Message.LoadMoreActivitiesClicked
    ): StudyPlanWidgetReducerResult =
        state.copy(
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(sectionContentStatus = SectionContentStatus.NEXT_PAGE_LOADING)
            }
        ) to setOf(
            InternalAction.LogAnalyticEvent(StudyPlanLoadMoreActivitiesClickedHSAnalyticEvent(message.sectionId)),
            InternalAction.FetchLearningActivities(
                sectionId = message.sectionId,
                activitiesIds = state.getNextRootTopicsActivitiesToBeLoaded(message.sectionId),
                sentryTransaction = getFetchLearningActivitiesSentryTransaction(state, message.sectionId)
            )
        )

    private fun handleExpandCompletedActivitiesClicked(
        state: State,
        message: Message.ExpandCompletedActivitiesClicked
    ): StudyPlanWidgetReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StudyPlanExpandCompletedActivitiesClickedHSAnalyticEvent(message.sectionId)
            ),
            InternalAction.FetchLearningActivities(
                sectionId = message.sectionId,
                activitiesIds = state.getActivitiesBeforeCurrentActivityToBeLoaded(message.sectionId),
                sentryTransaction = getFetchLearningActivitiesSentryTransaction(state, message.sectionId)
            )
        )

    private fun changeSectionExpanse(
        state: State,
        sectionId: Long,
        shouldLogAnalyticEvent: Boolean
    ): StudyPlanWidgetReducerResult {
        val section =
            state.studyPlanSections[sectionId] ?: return state to emptySet()
        val isExpanded = !section.isExpanded

        val logAnalyticEventAction = if (shouldLogAnalyticEvent) {
            InternalAction.LogAnalyticEvent(StudyPlanClickedSectionHyperskillAnalyticEvent(sectionId, isExpanded))
        } else {
            null
        }

        fun updateSectionState(
            sectionContentStatus: SectionContentStatus
        ): State =
            state.copy(
                studyPlanSections = state.studyPlanSections.update(
                    sectionId,
                    section.copy(isExpanded = isExpanded, sectionContentStatus = sectionContentStatus)
                )
            )

        return if (isExpanded) {
            when (val contentStatus = section.sectionContentStatus) {
                SectionContentStatus.IDLE,
                SectionContentStatus.ERROR -> {
                    val sentryTransaction = getFetchLearningActivitiesSentryTransaction(state, sectionId)
                    updateSectionState(SectionContentStatus.FIRST_PAGE_LOADING) to setOfNotNull(
                        InternalAction.FetchLearningActivities(
                            sectionId = sectionId,
                            activitiesIds = getPaginatedActivitiesIds(
                                section = section,
                                isLearningPathDividedTrackTopicsEnabled = state.isLearningPathDividedTrackTopicsEnabled
                            ),
                            sentryTransaction = sentryTransaction
                        ),
                        logAnalyticEventAction
                    )
                }

                // activities are loading at the moment or already loaded
                SectionContentStatus.FIRST_PAGE_LOADING,
                SectionContentStatus.NEXT_PAGE_LOADING,
                SectionContentStatus.FIRST_PAGE_LOADED,
                SectionContentStatus.ALL_PAGES_LOADED -> {
                    updateSectionState(contentStatus) to setOfNotNull(logAnalyticEventAction)
                }
            }
        } else {
            updateSectionState(section.sectionContentStatus) to setOfNotNull(logAnalyticEventAction)
        }
    }

    internal fun getPaginatedActivitiesIds(
        section: StudyPlanWidgetFeature.StudyPlanSectionInfo,
        isLearningPathDividedTrackTopicsEnabled: Boolean
    ): List<Long> =
        if (section.studyPlanSection.type == StudyPlanSectionType.ROOT_TOPICS &&
            section.studyPlanSection.nextActivityId != null &&
            !isLearningPathDividedTrackTopicsEnabled
        ) {
            val startIndex = section.studyPlanSection.firstRootTopicsActivityIndexToBeLoaded
            val endIndex =
                min(startIndex + (SECTION_ROOT_TOPICS_PAGE_SIZE - 1), section.studyPlanSection.activities.lastIndex)
            section.studyPlanSection.activities.slice(startIndex..endIndex)
        } else {
            section.studyPlanSection.activities
        }

    private fun handleActivityClicked(state: State, message: Message.ActivityClicked): StudyPlanWidgetReducerResult {
        val activity = state.activities[message.activityId] ?: return state to emptySet()

        val isActivityLocked = state.isActivityLocked(message.sectionId, message.activityId)

        val logAnalyticEventAction = InternalAction.LogAnalyticEvent(
            StudyPlanClickedActivityHyperskillAnalyticEvent(
                activityId = activity.id,
                activityType = activity.type?.value,
                activityTargetType = activity.targetType,
                activityTargetId = activity.targetId,
                isLocked = isActivityLocked
            )
        )

        val activityTargetAction =
            if (isActivityLocked) {
                Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.STUDY_PLAN)
            } else {
                LearningActivityTargetViewActionMapper
                    .mapLearningActivityToTargetViewAction(
                        activity = activity,
                        trackId = state.profile?.trackId,
                        projectId = state.profile?.projectId
                    )
                    .fold(
                        onSuccess = { Action.ViewAction.NavigateTo.LearningActivityTarget(it) },
                        onFailure = { InternalAction.CaptureSentryException(it) }
                    )
            }

        return state to setOf(activityTargetAction, logAnalyticEventAction)
    }

    private fun handleSubscribeClicked(state: State): StudyPlanWidgetReducerResult =
        if (state.isPaywallShown()) {
            state to setOf(
                InternalAction.LogAnalyticEvent(StudyPlanClickedSubscribeHyperskillAnalyticEvent),
                Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.STUDY_PLAN)
            )
        } else {
            state to emptySet()
        }

    private fun handleFetchSubscriptionLimitTypeResult(
        state: State,
        message: InternalMessage.FetchSubscriptionLimitTypeResult
    ): StudyPlanWidgetReducerResult =
        state.copy(subscriptionLimitType = message.subscriptionLimitType) to emptySet()

    private fun handleSubscriptionLimitTypeChanged(
        state: State,
        message: InternalMessage.SubscriptionLimitTypeChanged
    ): StudyPlanWidgetReducerResult =
        state.copy(subscriptionLimitType = message.subscriptionLimitType) to emptySet()

    private fun getFetchLearningActivitiesSentryTransaction(
        state: State,
        sectionId: Long
    ): HyperskillSentryTransaction =
        HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(
            isCurrentSection = sectionId == state.getCurrentSection()?.id
        )

    private fun <K, V> Map<K, V>.update(key: K, value: V): Map<K, V> =
        this.toMutableMap().apply {
            this[key] = value
        }

    private inline fun <K, V> Map<K, V>.update(
        key: K,
        block: (V) -> V
    ): Map<K, V> {
        val section = this[key]
        return if (section != null) {
            val newValue = block(section)
            this.update(key, newValue)
        } else {
            this
        }
    }
}