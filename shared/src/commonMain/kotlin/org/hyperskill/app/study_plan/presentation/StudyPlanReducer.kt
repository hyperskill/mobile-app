package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StudyPlanReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            Message.Initialize -> {
                state.copy(contentStatus = StudyPlanFeature.ContentStatus.LOADING) to setOf(Action.FetchStudyPlan)
            }
            is StudyPlanFeature.StudyPlanFetchResult.Success -> {
                state.copy(
                    studyPlan = message.studyPlan,
                    contentStatus = StudyPlanFeature.ContentStatus.SUCCESS
                ) to emptySet()
            }
            StudyPlanFeature.StudyPlanFetchResult.Failed -> {
                state.copy(contentStatus = StudyPlanFeature.ContentStatus.ERROR) to emptySet()
            }
        }
}