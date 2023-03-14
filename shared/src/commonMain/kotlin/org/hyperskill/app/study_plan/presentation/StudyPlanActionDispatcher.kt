package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanActionDispatcher(
    config: ActionDispatcherOptions
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        TODO("Not implemented")
    }
}