package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizHintsComponentImpl(private val appGraph: AppGraph, private val stepRoute: StepRoute) : StepQuizHintsComponent {
    private val stepQuizHintsInteractor: StepQuizHintsInteractor =
        StepQuizHintsInteractor(
            appGraph.buildDiscussionsDataComponent().discussionsRepository,
            appGraph.buildUserStorageComponent().userStorageRepository,
            appGraph.buildCommentsDataComponent().commentsRepository
        )

    override val stepQuizHintsFeature: Feature<StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
        get() = StepQuizHintsFeatureBuilder.build(
            stepRoute,
            stepQuizHintsInteractor,
            appGraph.buildLikesDataComponent().likesInteractor,
            appGraph.buildCommentsDataComponent().commentsInteractor,
            appGraph.buildReactionsDataComponent().reactionsInteractor,
            appGraph.buildUserStorageComponent().userStorageInteractor,
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )
}