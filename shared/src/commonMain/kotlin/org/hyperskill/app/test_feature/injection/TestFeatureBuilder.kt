package org.hyperskill.app.test_feature

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.test_feature.TestFeature.Action
import org.hyperskill.app.test_feature.TestFeature.Message
import org.hyperskill.app.test_feature.TestFeature.State
import org.hyperskill.app.test_feature.TestReducer
import org.hyperskill.app.test_feature.TestActionDispatcher
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object TestFeatureBuilder {
    private const val LOG_TAG = "TestFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val testReducer =
            TestReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val testActionDispatcher = TestActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = TODO("add state implementation"),
            reducer = testReducer
        ).wrapWithActionDispatcher(testActionDispatcher)
    }
}