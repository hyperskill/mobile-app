package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizHintsComponentImpl(private val appGraph: AppGraph) : StepQuizHintsComponent {
    private val discussionsInteractor: DiscussionsInteractor =
        appGraph.buildDiscussionsDataComponent().discussionsInteractor

    private val userStorageInteractor: UserStorageInteractor =
        appGraph.buildUserStorageComponent().userStorageInteractor

    private val stepQuizHintsInteractor: StepQuizHintsInteractor =
        StepQuizHintsInteractor(discussionsInteractor, userStorageInteractor)

    override val stepQuizHintsFeature: Feature<StepQuizHintsFeature.State, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
        get() = StepQuizHintsFeatureBuilder.build(
            stepQuizHintsInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildLikesDataComponent().likesInteractor,
            appGraph.buildCommentsDataComponent().commentsInteractor,
            appGraph.buildReactionsDataComponent().reactionsInteractor,
            appGraph.buildUserStorageComponent().userStorageInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}