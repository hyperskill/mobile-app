package org.hyperskill.app.test_feature

import org.hyperskill.app.test_feature.TestFeature.Action
import org.hyperskill.app.test_feature.TestFeature.InternalAction
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class TestActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is TestFeature.InternalAction.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}