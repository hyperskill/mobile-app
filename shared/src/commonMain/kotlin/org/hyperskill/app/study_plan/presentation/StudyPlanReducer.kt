package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.TargetState
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StudyPlanReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.Initialize -> {
                state.copy(sectionsStatus = StudyPlanFeature.SectionsStatus.LOADING) to setOf(Action.FetchStudyPlan)
            }
            is StudyPlanFeature.StudyPlanFetchResult.Success -> {
                state.copy(studyPlan = message.studyPlan) to
                    setOf(Action.FetchSections(message.studyPlan.sections))
            }
            is StudyPlanFeature.SectionsFetchResult.Success -> {
                val visibleSections = message.sections.filter { it.isVisible }
                val fetchFirstSectionActivitiesAction = visibleSections.firstOrNull()?.let { firstSection ->
                    Action.FetchActivities(
                        sectionId = firstSection.id,
                        activitiesIds = firstSection.activities
                    )
                }
                state.copy(
                    studyPlanSections = visibleSections,
                    sectionsStatus = StudyPlanFeature.SectionsStatus.SUCCESS
                ) to setOfNotNull(fetchFirstSectionActivitiesAction)
            }
            is StudyPlanFeature.LearningActivitiesFetchResult.Success ->
                handleLearningActivitiesFetchSuccess(state, message)
            StudyPlanFeature.StudyPlanFetchResult.Failed,
            StudyPlanFeature.SectionsFetchResult.Failed,
            StudyPlanFeature.LearningActivitiesFetchResult.Failed -> {
                state.copy(sectionsStatus = StudyPlanFeature.SectionsStatus.ERROR) to emptySet()
            }
        }

    private fun handleLearningActivitiesFetchSuccess(
        state: State,
        message: StudyPlanFeature.LearningActivitiesFetchResult.Success
    ): Pair<State, Set<Action>> {
        val currentSectionActivities = state.activities.getOrElse(message.sectionId) { emptyList() }
        val filteredActivities = message.activities.dropWhile { it.state != TargetState.TODO }
        val actualSectionActivities = filteredActivities.union(currentSectionActivities)
        val actualSectionEntry = message.sectionId to actualSectionActivities
        return state.copy(
            activities = state.activities + actualSectionEntry
        ) to emptySet()
    }
}