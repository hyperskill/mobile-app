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
            StudyPlanFeature.StudyPlanFetchResult.Failed,
            StudyPlanFeature.SectionsFetchResult.Failed,
            StudyPlanFeature.LearningActivitiesFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanFeature.ContentStatus.ERROR) to emptySet()
            }
        }

    private fun handleSectionsFetchSuccess(
        state: State,
        message:  StudyPlanFeature.SectionsFetchResult.Success
    ): StudyPlanReducerResult {
        val visibleSections = message.sections.filter { it.isVisible }
        val firstVisibleSection = visibleSections.firstOrNull()

        val studyPlanSections = visibleSections.mapIndexed { index, studyPlanSection ->
            studyPlanSection.id to StudyPlanFeature.StudyPlanSectionInfo(
                studyPlanSection = studyPlanSection,
                isExpanded = index == 0 // Expand first section
            )
        }.toMap()

        val sectionsContentStatuses = if (firstVisibleSection != null) {
            mapOf(firstVisibleSection.id to StudyPlanFeature.ContentStatus.LOADING)
        } else {
            emptyMap()
        }

        val fetchFirstSectionActivitiesAction = firstVisibleSection?.let { firstSection ->
            Action.FetchActivities(
                sectionId = firstSection.id,
                activitiesIds = firstSection.activities
            )
        }

        return state.copy(
            studyPlanSections = studyPlanSections,
            sectionsStatus = StudyPlanFeature.ContentStatus.LOADED,
            sectionsContentStatuses = sectionsContentStatuses
        ) to setOfNotNull(fetchFirstSectionActivitiesAction)
    }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanFeature.LearningActivitiesFetchResult.Success
    ): StudyPlanReducerResult {
        val currentSectionActivities = state.activities.getOrElse(message.sectionId) { emptyList() }
        val filteredActivities = message.activities.dropWhile { it.state != TargetState.TODO }
        val actualSectionActivities = filteredActivities.union(currentSectionActivities)
        val actualSectionEntry = message.sectionId to actualSectionActivities
        return state.copy(
            activities = state.activities + actualSectionEntry
        ) to emptySet()
    }
}