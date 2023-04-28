package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivityTargetType
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedActivityHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryActivitiesLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanClickedSectionHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.analytic.StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.domain.model.StudyPlanStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.STUDY_PLAN_FETCH_INTERVAL
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanWidgetReducerResult = Pair<State, Set<Action>>

class StudyPlanWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StudyPlanWidgetReducerResult =
        when (message) {
            is Message.Initialize ->
                coldContentFetch()
            is StudyPlanWidgetFeature.StudyPlanFetchResult.Success ->
                handleStudyPlanFetchSuccess(state, message)
            is StudyPlanWidgetFeature.SectionsFetchResult.Success ->
                handleSectionsFetchSuccess(state, message)
            is Message.RetryContentLoading ->
                handleRetryContentLoading()
            is Message.RetryActivitiesLoading ->
                handleRetryActivitiesLoading(state, message)
            is Message.ReloadContentInBackground ->
                state.copy(
                    studyPlanSections = state.studyPlanSections.mapValues { (sectionId, sectionInfo) ->
                        sectionInfo.copy(
                            contentStatus = if (sectionId == state.firstSection()?.id) {
                                sectionInfo.contentStatus
                            } else {
                                StudyPlanWidgetFeature.ContentStatus.IDLE
                            }
                        )
                    }
                ) to setOf(InternalAction.FetchStudyPlan(showLoadingIndicators = false))
            is Message.PullToRefresh ->
                if (!state.isRefreshing) {
                    state.copy(isRefreshing = true) to setOf(
                        InternalAction.FetchStudyPlan(showLoadingIndicators = false)
                    )
                } else {
                    null
                }
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success ->
                handleLearningActivitiesFetchSuccess(state, message)
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed ->
                handleLearningActivitiesFetchFailed(state, message)
            is StudyPlanWidgetFeature.StudyPlanFetchResult.Failed,
            is StudyPlanWidgetFeature.SectionsFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.ERROR) to emptySet()
            }
            is StudyPlanWidgetFeature.TrackFetchResult.Success -> {
                state.copy(track = message.track) to emptySet()
            }
            is StudyPlanWidgetFeature.TrackFetchResult.Failed -> {
                null
            }
            is Message.SectionClicked ->
                changeSectionExpanse(state, message.sectionId, shouldLogAnalyticEvent = true)
            is Message.ActivityClicked ->
                handleActivityClicked(state, message.activityId)
            is Message.StageImplementUnsupportedModalGoToHomeClicked ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent()
                    ),
                    Action.ViewAction.NavigateTo.Home
                )
            is Message.StageImplementUnsupportedModalHiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent()
                    )
                )
            is Message.StageImplementUnsupportedModalShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StudyPlanStageImplementUnsupportedModalShownHyperskillAnalyticEvent()
                    )
                )
        } ?: (state to emptySet())

    private fun coldContentFetch(): StudyPlanWidgetReducerResult =
        State(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING) to
            setOf(InternalAction.FetchStudyPlan())

    private fun handleStudyPlanFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.StudyPlanFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val actions = if (message.studyPlan.status != StudyPlanStatus.READY) {
            setOf(
                InternalAction.FetchStudyPlan(
                    delayBeforeFetching = STUDY_PLAN_FETCH_INTERVAL * message.attemptNumber,
                    attemptNumber = message.attemptNumber + 1
                )
            )
        } else {
            setOfNotNull(
                InternalAction.FetchSections(message.studyPlan.sections),
                message.studyPlan.trackId?.let(InternalAction::FetchTrack)
            )
        }

        return if (message.showLoadingIndicators) {
            State(
                studyPlan = message.studyPlan,
                sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING
            ) to actions
        } else {
            state.copy(studyPlan = message.studyPlan) to actions
        }
    }

    private fun handleSectionsFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.SectionsFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val supportedSections = message.sections.filter { it.isSupportedInStudyPlan }

        val studyPlanSections = supportedSections.associate { studyPlanSection ->
            studyPlanSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = false,
                contentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE
            )
        }

        val sortedSupportedSectionsIds =
            if (state.studyPlan != null) {
                val supportedSectionsIds = supportedSections.map { it.id }.toSet()
                state.studyPlan.sections.intersect(supportedSectionsIds).toList()
            } else {
                return state to emptySet()
            }

        val loadedSectionsState = state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED,
            isRefreshing = false
        )

        return if (sortedSupportedSectionsIds.isNotEmpty()) {
            changeSectionExpanse(
                loadedSectionsState,
                sortedSupportedSectionsIds.first(),
                shouldLogAnalyticEvent = false
            )
        } else {
            loadedSectionsState to emptySet()
        }
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success
    ): StudyPlanWidgetReducerResult =
        state.copy(
            activities = state.activities.toMutableMap().apply {
                putAll(
                    message.activities.associateBy { it.id }
                )
            },
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED)
            }
        ) to emptySet()

    private fun handleLearningActivitiesFetchFailed(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed
    ): StudyPlanWidgetReducerResult =
        state.copy(
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.ERROR)
            }
        ) to emptySet()

    private fun handleRetryContentLoading(): StudyPlanWidgetReducerResult {
        val (state, actions) = coldContentFetch()
        return state to actions + InternalAction.LogAnalyticEvent(
            StudyPlanClickedRetryContentLoadingHyperskillAnalyticEvent()
        )
    }

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
            InternalAction.FetchActivities(
                sectionId = message.sectionId,
                activitiesIds = section.studyPlanSection.activities,
                sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(
                    isCurrentSection = message.sectionId == state.firstSection()?.id
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
                    updateSectionState(StudyPlanWidgetFeature.ContentStatus.LOADING) to setOfNotNull(
                        InternalAction.FetchActivities(
                            sectionId = sectionId,
                            activitiesIds = section.studyPlanSection.activities,
                            sentryTransaction = HyperskillSentryTransactionBuilder.buildStudyPlanWidgetFetchLearningActivities(
                                isCurrentSection = sectionId == state.firstSection()?.id
                            )
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

        if (!activity.isCurrent) {
            return state to setOf(logAnalyticEventAction)
        }

        val viewAction = when (activity.type) {
            LearningActivityType.IMPLEMENT_STAGE -> {
                val projectId = state.studyPlan?.projectId
                if (projectId != null && activity.targetType == LearningActivityTargetType.STAGE) {
                    if (activity.isIdeRequired) {
                        Action.ViewAction.ShowStageImplementUnsupportedModal
                    } else {
                        Action.ViewAction.NavigateTo.StageImplement(
                            stageId = activity.targetId,
                            projectId = projectId
                        )
                    }
                } else {
                    null
                }
            }
            LearningActivityType.LEARN_TOPIC -> {
                if (activity.targetType == LearningActivityTargetType.STEP) {
                    Action.ViewAction.NavigateTo.StepScreen(StepRoute.Learn(activity.targetId))
                } else {
                    null
                }
            }
            else -> null
        }
        return state to setOfNotNull(viewAction, logAnalyticEventAction)
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