package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.TargetState
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StudyPlanReducerResult = Pair<State, Set<Action>>

internal class StudyPlanReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StudyPlanReducerResult =
        when (message) {
            Message.Initialize -> {
                state.copy(sectionsStatus = StudyPlanFeature.ContentStatus.LOADING) to setOf(Action.FetchStudyPlan)
            }
            is StudyPlanFeature.StudyPlanFetchResult.Success -> {
                state.copy(studyPlan = message.studyPlan) to
                    setOf(Action.FetchSections(message.studyPlan.sections))
            }
            is StudyPlanFeature.SectionsFetchResult.Success ->
                handleSectionsFetchSuccess(state, message)
            is StudyPlanFeature.LearningActivitiesFetchResult.Success ->
                handleLearningActivitiesFetchSuccess(state, message)
            is StudyPlanFeature.LearningActivitiesFetchResult.Failed ->
                handleLearningActivitiesFetchFailed(state, message)
            StudyPlanFeature.StudyPlanFetchResult.Failed,
            StudyPlanFeature.SectionsFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanFeature.ContentStatus.ERROR) to emptySet()
            }
            is Message.SectionExpanseChanged ->
                handleSectionExpanseChanged(state, message)
        }

    private fun handleSectionsFetchSuccess(
        state: State,
        message: StudyPlanFeature.SectionsFetchResult.Success
    ): StudyPlanReducerResult {
        val visibleSections = message.sections.filter { it.isVisible }
        val firstVisibleSection = visibleSections.firstOrNull()

        val studyPlanSections = visibleSections.mapIndexed { index, studyPlanSection ->
            studyPlanSection.id to StudyPlanFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = index == 0, // Expand first section
                contentStatus = StudyPlanFeature.ContentStatus.IDLE
            )
        }.toMap()

        val loadedSectionsState = state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanFeature.ContentStatus.LOADED
        )

        return if (firstVisibleSection != null) {
            fetchActivities(loadedSectionsState, firstVisibleSection.id, firstVisibleSection.activities)
        } else {
            loadedSectionsState to emptySet()
        }
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanFeature.LearningActivitiesFetchResult.Success
    ): StudyPlanReducerResult {
        val currentSectionActivities = state.activities.getOrElse(message.sectionId) { emptyList() }
        val filteredActivities = message.activities.dropWhile { it.state != TargetState.TODO }
        val actualSectionActivities = filteredActivities.union(currentSectionActivities)
        val actualActivities =
            state.activities + (message.sectionId to actualSectionActivities)

        return state.copy(
            activities = actualActivities,
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanFeature.ContentStatus.LOADED)
            }
        ) to emptySet()
    }

    private fun handleLearningActivitiesFetchFailed(
        state: State,
        message: StudyPlanFeature.LearningActivitiesFetchResult.Failed
    ): StudyPlanReducerResult =
        state.copy(
            studyPlanSections = state.studyPlanSections.update(message.sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanFeature.ContentStatus.ERROR)
            }
        ) to emptySet()

    private fun handleSectionExpanseChanged(
        state: State,
        message: Message.SectionExpanseChanged
    ): StudyPlanReducerResult {
        val section =
            state.studyPlanSections[message.sectionId] ?: return state to emptySet()

        val sectionUpdatedState = state.copy(
            studyPlanSections = state.studyPlanSections.set(
                message.sectionId,
                section.copy(isExpanded = message.isExpanded)
            )
        )

        return if (message.isExpanded) {
            when (section.contentStatus) {
                StudyPlanFeature.ContentStatus.IDLE,
                StudyPlanFeature.ContentStatus.ERROR -> {
                    fetchActivities(sectionUpdatedState, message.sectionId, section.studyPlanSection.activities)
                }

                // activities are loading at the moment or already loaded
                StudyPlanFeature.ContentStatus.LOADING,
                StudyPlanFeature.ContentStatus.LOADED -> {
                    sectionUpdatedState to emptySet()
                }
            }
        } else {
            sectionUpdatedState to emptySet()
        }
    }

    private fun fetchActivities(state: State, sectionId: Long, activitiesIds: List<Long>): StudyPlanReducerResult =
        state.copy(
            studyPlanSections = state.studyPlanSections.update(sectionId) { sectionInfo ->
                sectionInfo.copy(contentStatus = StudyPlanFeature.ContentStatus.LOADING)
            }
        ) to setOf(
            Action.FetchActivities(
                sectionId = sectionId,
                activitiesIds = activitiesIds
            )
        )

    private fun <K, V> Map<K, V>.set(key: K, value: V): Map<K, V> =
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
            this.set(key, newValue)
        } else {
            this
        }
    }
}