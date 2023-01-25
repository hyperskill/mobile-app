package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.model.StepQuizHintsViewState
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizHintsComponentImpl(private val appGraph: AppGraph) : StepQuizHintsComponent {
    private val stepQuizHintsInteractor: StepQuizHintsInteractor =
        StepQuizHintsInteractor(
            appGraph.buildDiscussionsDataComponent().discussionsRepository,
            appGraph.buildUserStorageComponent().userStorageRepository,
            appGraph.buildCommentsDataComponent().commentsRepository
        )

    override val stepQuizHintsFeature: Feature<StepQuizHintsViewState, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
        get() = StepQuizHintsFeatureBuilder.build(
            stepQuizHintsInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildLikesDataComponent().likesInteractor,
            appGraph.buildCommentsDataComponent().commentsInteractor,
            appGraph.buildReactionsDataComponent().reactionsInteractor,
            appGraph.buildUserStorageComponent().userStorageInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )
}