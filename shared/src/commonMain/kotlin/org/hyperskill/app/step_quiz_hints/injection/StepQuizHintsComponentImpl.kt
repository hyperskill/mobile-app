package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor
import org.hyperskill.app.step_quiz_hints.domain.interactor.StepQuizHintsInteractor
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

class StepQuizHintsComponentImpl(appGraph: AppGraph) : StepQuizHintsComponent {
    private val discussionsInteractor: DiscussionsInteractor =
        appGraph.buildDiscussionsDataComponent().discussionsInteractor

    private val userStorageInteractor: UserStorageInteractor =
        appGraph.buildUserStorageComponent().userStorageInteractor

    override val stepQuizHintsInteractor: StepQuizHintsInteractor
        get() = StepQuizHintsInteractor(discussionsInteractor, userStorageInteractor)
}