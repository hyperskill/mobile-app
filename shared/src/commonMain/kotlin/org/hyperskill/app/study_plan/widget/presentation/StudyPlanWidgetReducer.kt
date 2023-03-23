package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanWidgetReducerResult = Pair<State, Set<Action>>

class StudyPlanWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StudyPlanWidgetReducerResult =
        when (message) {
            Message.Initialize -> {
                state.copy(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADING) to setOf(
                    InternalAction.FetchStudyPlan
                )
            }
            is StudyPlanWidgetFeature.StudyPlanFetchResult.Success -> {
                state.copy(studyPlan = message.studyPlan) to
                    setOfNotNull(
                        InternalAction.FetchSections(message.studyPlan.sections),
                        message.studyPlan.trackId?.let(InternalAction::FetchTrack)
                    )
            }
            is StudyPlanWidgetFeature.SectionsFetchResult.Success ->
                handleSectionsFetchSuccess(state, message)
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success ->
                handleLearningActivitiesFetchSuccess(state, message)
            is StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed ->
                handleLearningActivitiesFetchFailed(state, message)
            StudyPlanWidgetFeature.StudyPlanFetchResult.Failed,
            StudyPlanWidgetFeature.SectionsFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanWidgetFeature.ContentStatus.ERROR) to emptySet()
            }
            is StudyPlanWidgetFeature.TrackFetchResult.Success -> {
                state.copy(track = message.track) to emptySet()
            }
            StudyPlanWidgetFeature.TrackFetchResult.Failed -> {
                state to emptySet()
            }
            is Message.SectionClicked ->
                changeSectionExpanse(state, message.sectionId)
            is Message.ActivityClicked -> handleActivityClicked(state, message.activityId)
        }

    private fun handleSectionsFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.SectionsFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        val visibleSections = message.sections.filter { it.isVisible }
        val firstVisibleSection = visibleSections.firstOrNull()

        val studyPlanSections = visibleSections.associate { studyPlanSection ->
            studyPlanSection.id to StudyPlanWidgetFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = false,
                contentStatus = StudyPlanWidgetFeature.ContentStatus.IDLE
            )
        }

        val loadedSectionsState = state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanWidgetFeature.ContentStatus.LOADED
        )

        return if (firstVisibleSection != null) {
            changeSectionExpanse(loadedSectionsState, firstVisibleSection.id)
        } else {
            loadedSectionsState to emptySet()
        }
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success
    ): StudyPlanWidgetReducerResult {
        return state.copy(
            activities = state.activities.toMutableMap().apply {
                putAll(
                    message.activities.associateBy { it.id }
                )
            },
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanWidgetFeature.ContentStatus.LOADED)
            }
        ) to emptySet()
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

    private fun changeSectionExpanse(
        state: State,
        sectionId: Long
    ): StudyPlanWidgetReducerResult {
        val section =
            state.studyPlanSections[sectionId] ?: return state to emptySet()
        val isExpanded = !section.isExpanded

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
                    updateSectionState(StudyPlanWidgetFeature.ContentStatus.LOADING) to setOf(
                        InternalAction.FetchActivities(
                            sectionId = sectionId,
                            activitiesIds = section.studyPlanSection.activities
                        )
                    )
                }

                // activities are loading at the moment or already loaded
                StudyPlanWidgetFeature.ContentStatus.LOADING,
                StudyPlanWidgetFeature.ContentStatus.LOADED -> {
                    updateSectionState() to emptySet()
                }
            }
        } else {
            updateSectionState() to emptySet()
        }
    }

    private fun handleActivityClicked(state: State, activityId: Long): StudyPlanWidgetReducerResult {
        val activity = state.activities[activityId]
        if (activity?.isCurrent != true) return state to emptySet()
        val action = when (activity.type) {
            LearningActivityType.IMPLEMENT_STAGE -> {
                val projectId = state.studyPlan?.projectId
                if (projectId != null) {
                    Action.ViewAction.NavigateTo.StageImplementation(stageId = activity.targetId, projectId = projectId)
                } else {
                    null
                }
            }
            else -> null
        }
        return state to setOfNotNull(action)
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