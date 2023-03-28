package org.hyperskill.app.study_plan.screen.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanScreenActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<StudyPlanScreenFeature.Action, StudyPlanScreenFeature.Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: StudyPlanScreenFeature.Action) {
        when (action) {
            is StudyPlanScreenFeature.InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.analyticEvent)
            }
            else -> {
                // no op
            }
        }
    }
}