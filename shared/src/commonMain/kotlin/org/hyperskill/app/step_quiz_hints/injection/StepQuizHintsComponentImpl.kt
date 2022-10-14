package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

class StepQuizHintsComponentImpl(appGraph: AppGraph) : StepQuizHintsComponent {
    private val commentsDataInteractor: CommentsDataInteractor =
        appGraph.buildCommentsDataComponent().commentsDataInteractor

    private val userStorageInteractor: UserStorageInteractor =
        appGraph.buildUserStorageComponent().userStorageInteractor

    override val stepQuizHintsInteractor: StepQuizHintsInteractor
        get() = StepQuizHintsInteractor(commentsDataInteractor, userStorageInteractor)
}