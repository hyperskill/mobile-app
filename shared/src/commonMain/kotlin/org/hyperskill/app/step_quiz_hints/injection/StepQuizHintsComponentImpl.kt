package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.MainStepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer

internal class StepQuizHintsComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepQuizHintsComponent {
    private val stepQuizHintsInteractor: StepQuizHintsInteractor =
        StepQuizHintsInteractor(
            discussionsRepository = appGraph.buildDiscussionsDataComponent().discussionsRepository,
            userStorageRepository = appGraph.buildUserStorageComponent().userStorageRepository,
            commentsRepository = appGraph.buildCommentsDataComponent().commentsRepository
        )

    override val stepQuizHintsReducer: StepQuizHintsReducer
        get() = StepQuizHintsReducer(stepRoute = stepRoute)

    private val mainStepQuizHintsActionDispatcher: MainStepQuizHintsActionDispatcher
        get() = MainStepQuizHintsActionDispatcher(
            config = ActionDispatcherOptions(),
            stepQuizHintsInteractor = stepQuizHintsInteractor,
            likesInteractor = appGraph.buildLikesDataComponent().likesInteractor,
            commentsRepository = appGraph.buildCommentsDataComponent().commentsRepository,
            reactionsInteractor = appGraph.buildReactionsDataComponent().reactionsInteractor,
            userStorageInteractor = appGraph.buildUserStorageComponent().userStorageInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )

    override val stepQuizHintsActionDispatcher: StepQuizHintsActionDispatcher
        get() = StepQuizHintsActionDispatcher(
            mainStepQuizHintsActionDispatcher,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}