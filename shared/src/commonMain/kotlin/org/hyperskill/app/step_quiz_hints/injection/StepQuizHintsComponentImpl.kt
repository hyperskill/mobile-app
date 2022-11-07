package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor
import ru.nobird.app.presentation.redux.feature.Feature

class StepQuizHintsComponentImpl(private val appGraph: AppGraph) : StepQuizHintsComponent {
    private val commentsDataInteractor: CommentsDataInteractor =
        appGraph.buildCommentsDataComponent().commentsDataInteractor

    private val userStorageInteractor: UserStorageInteractor =
        appGraph.buildUserStorageComponent().userStorageInteractor

    private val stepQuizHintsInteractor: StepQuizHintsInteractor =
        StepQuizHintsInteractor(commentsDataInteractor, userStorageInteractor)

    override val stepQuizHintsFeature: Feature<StepQuizHintsFeature.State, StepQuizHintsFeature.Message, StepQuizHintsFeature.Action>
        get() = StepQuizHintsFeatureBuilder.build(
            commentsDataInteractor,
            userStorageInteractor,
            stepQuizHintsInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.buildProfileDataComponent().profileInteractor
        )
}