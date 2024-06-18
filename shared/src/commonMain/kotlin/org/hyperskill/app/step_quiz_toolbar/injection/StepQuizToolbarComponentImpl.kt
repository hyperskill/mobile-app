package org.hyperskill.app.step_quiz_toolbar.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.presentation.MainStepQuizToolbarActionDispatcher
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarActionDispatcher
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

internal class StepQuizToolbarComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepQuizToolbarComponent {

    companion object {
        private const val LOG_TAG = "StepQuizToolbarFeature"
    }

    override val stepQuizToolbarReducer: StepQuizToolbarReducer
        get() = StepQuizToolbarReducer(stepRoute)

    private val mainStepQuizToolbarActionDispatcher: MainStepQuizToolbarActionDispatcher
        get() = MainStepQuizToolbarActionDispatcher(
            config = ActionDispatcherOptions(),
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            logger = appGraph.loggerComponent.logger.withTag(LOG_TAG)
        )

    override val stepQuizToolbarActionDispatcher: StepQuizToolbarActionDispatcher
        get() = StepQuizToolbarActionDispatcher(
            mainStepQuizToolbarActionDispatcher,
            appGraph.analyticComponent.analyticInteractor
        )
}