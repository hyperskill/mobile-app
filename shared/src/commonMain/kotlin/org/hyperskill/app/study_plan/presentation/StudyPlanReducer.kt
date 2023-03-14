package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StudyPlanReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        TODO("Not implemented")
}