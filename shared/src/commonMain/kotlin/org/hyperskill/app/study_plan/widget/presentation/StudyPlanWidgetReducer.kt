package org.hyperskill.app.study_plan.widget.presentation

import kotlin.math.max
import kotlin.math.min
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.presentation.mapper.LearningActivityTargetViewActionMapper
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedActivityHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSectionHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.study_plan.widget.domain.mapper.LearningActivityToTopicProgressMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalMessage
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.State
import org.hyperskill.app.subscriptions.domain.model.isFreemium
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
            is InternalMessage.ReloadContentInBackground -> {
                val currentSectionId = state.getCurrentSection()?.id
                state.copy(
                    studyPlanSections = state.studyPlanSections.mapValues { (sectionId, sectionInfo) ->
                        sectionInfo.copy(
                            contentStatus = if (sectionId == currentSectionId) {
                                sectionInfo.contentStatus
                            } else {
                                StudyPlanWidgetFeature.ContentStatus.IDLE
                            }
                        )
                    }
                ) to getContentFetchActions(forceUpdate = true)
            }
            is Message.PullToRefresh ->
                if (!state.isRefreshing) {
                    state.copy(isRefreshing = true) to getContentFetchActions(forceUpdate = true)
                } else {
                    null
                }
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
                handleActivityClicked(state, message.activityId)
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
        setOf(
            InternalAction.FetchLearningActivitiesWithSections(),
            InternalAction.FetchProfile,
            InternalAction.UpdateCurrentStudyPlanState(forceUpdate)
        )

    private fun handleLearningActivitiesWithSectionsFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesWithSectionsFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val learningActivitiesIds = message.learningActivities.map { it.id }.toSet()

        /**
         * The current section is the section that contains learning activities that were returned.
         * There could be a situation when the API returns activities from not first visible section.
         *
         * So, we should hide all visible sections that above of current section.
         * For example, the first visible section contains only not supported activities.
         */
        var visibleSections = message.studyPlanSections.filter { it.isVisible }
        val currentSectionIndex = visibleSections
            .indexOfFirst { studyPlanSection ->
                studyPlanSection.activities.toSet().intersect(learningActivitiesIds).isNotEmpty()
            }
            .takeIf { it != -1 }
            ?: return state.copy(
                studyPlanSections = emptyMap(),
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
                isRefreshing = false
            ) to emptySet()
        val currentSectionId = visibleSections[currentSectionIndex].id
        visibleSections = visibleSections.slice(from = currentSectionIndex)

        val supportedSections = visibleSections
            .filter { studyPlanSection ->
                // ALTAPPS-1186: We should hide next project section for freemium users
                if (message.subscription.isFreemium) {
                    studyPlanSection.type != StudyPlanSectionType.NEXT_PROJECT
                } else {
                    true
                }
            }

        val studyPlanSections = supportedSections.associate { studyPlanSection ->
            studyPlanSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = studyPlanSection.id == currentSectionId,
                contentStatus = if (studyPlanSection.id == currentSectionId) {
                    StudyPlanWidgetFeature.ContentStatus.LOADED
                } else {
                    StudyPlanWidgetFeature.ContentStatus.IDLE
                }
            )
        }

        val loadedSectionsState = state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isRefreshing = false
        )

        return if (loadedSectionsState.studyPlanSections.isNotEmpty()) {
            handleLearningActivitiesFetchSuccess(
                state = loadedSectionsState,
                message = StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                    sectionId = currentSectionId,
                    activities = message.learningActivities
                )
            )
        } else {
            loadedSectionsState to emptySet()
        }
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val nextState = state.copy(
            activities = state.activities.toMutableMap().apply {
                putAll(message.activities.associateBy { it.id })
                // ALTAPPS-743: We should remove activities that are not in the new list
                // (e.g. when user has completed or skipped some activities)
                val activitiesIds =
                    state.studyPlanSections[message.sectionId]?.studyPlanSection?.activities?.toSet() ?: emptySet()
                val activitiesIdsToRemove = activitiesIds - message.activities.map { it.id }.toSet()
                activitiesIdsToRemove.forEach { remove(it) }
            },
            // ALTAPPS-786: We should hide sections without available activities to avoid blocking study plan
            studyPlanSections = if (message.activities.isEmpty()) {
                state.studyPlanSections.toMutableMap().apply {
                    remove(message.sectionId)
                }
            } else {
                state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                    sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED)
                }
            }
        )

        val isFetchedActivitiesForCurrentSection = message.sectionId == state.getCurrentSection()?.id

        // ALTAPPS-786: We should expand next section if current section doesn't have available activities
        val (resultState, resultActions) =
            if (isFetchedActivitiesForCurrentSection && message.activities.isEmpty()) {
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
                setOf(putFetchedLearningActivitiesProgressesToCacheAction(message.activities))
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
                sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.ERROR)
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
                sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADING)
            }
        ) to setOf(
            InternalAction.FetchLearningActivities(
                sectionId = message.sectionId,
                activitiesIds = getPaginatedActivitiesIds(
                    section = section,
                    isLearningPathDividedTrackTopicsEnabled = state.isLearningPathDividedTrackTopicsEnabled
                ),
                sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(
                    isCurrentSection = message.sectionId == state.getCurrentSection()?.id
                )
            ),
            InternalAction.LogAnalyticEvent(
                StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent(message.sectionId)
            )
        )
    }

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

        fun updateSectionState(contentStatus: StudyPlanWidgetFeature.ContentStatus = section.contentStatus): State =
            state.copy(
                studyPlanSections = state.studyPlanSections.update(
                    sectionId,
                    section.copy(isExpanded = isExpanded, contentStatus = contentStatus)
                )
            )

        return if (isExpanded) {
            when (section.contentStatus) {
                StudyPlanWidgetFeature.ContentStatus.IDLE,
                StudyPlanWidgetFeature.ContentStatus.ERROR -> {
                    val sentryTransaction =
                        HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(
                            isCurrentSection = sectionId == state.getCurrentSection()?.id
                        )
                    updateSectionState(StudyPlanWidgetFeature.ContentStatus.LOADING) to setOfNotNull(
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
                StudyPlanWidgetFeature.ContentStatus.LOADING,
                StudyPlanWidgetFeature.ContentStatus.LOADED -> {
                    updateSectionState() to setOfNotNull(logAnalyticEventAction)
                }
            }
        } else {
            updateSectionState() to setOfNotNull(logAnalyticEventAction)
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
            val startIndex =
                max(0, section.studyPlanSection.activities.indexOf(section.studyPlanSection.nextActivityId))
            val endIndex =
                min(startIndex + (SECTION_ROOT_TOPICS_PAGE_SIZE - 1), section.studyPlanSection.activities.size - 1)
            section.studyPlanSection.activities.slice(startIndex..endIndex)
        } else {
            section.studyPlanSection.activities
        }

    private fun handleActivityClicked(state: State, activityId: Long): StudyPlanWidgetReducerResult {
        val activity = state.activities[activityId] ?: return state to emptySet()

        val logAnalyticEventAction = InternalAction.LogAnalyticEvent(
            StudyPlanClickedActivityHyperskillAnalyticEvent(
                activityId = activity.id,
                activityType = activity.type?.value,
                activityTargetType = activity.targetType?.value,
                activityTargetId = activity.targetId
            )
        )

        val activityTargetAction = LearningActivityTargetViewActionMapper
            .mapLearningActivityToTargetViewAction(
                activity = activity,
                trackId = state.profile?.trackId,
                projectId = state.profile?.projectId
            )
            .fold(
                onSuccess = { Action.ViewAction.NavigateTo.LearningActivityTarget(it) },
                onFailure = { InternalAction.CaptureSentryException(it) }
            )

        return state to setOf(activityTargetAction, logAnalyticEventAction)
    }

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