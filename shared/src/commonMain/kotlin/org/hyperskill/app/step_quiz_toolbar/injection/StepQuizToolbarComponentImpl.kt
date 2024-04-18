package org.hyperskill.app.step_quiz_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarActionDispatcher
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

internal class StepQuizToolbarComponentImpl(
    private val appGraph: AppGraph
) : StepQuizToolbarComponent {

    companion object {
        private const val LOG_TAG = "StepQuizToolbarFeature"
    }

    override val stepQuizToolbarReducer: StepQuizToolbarReducer
        get() = StepQuizToolbarReducer()

    override val stepQuizToolbarActionDispatcher: StepQuizToolbarActionDispatcher
        get() = StepQuizToolbarActionDispatcher(
            config = ActionDispatcherOptions(),
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger.withTag(LOG_TAG)
        )
}