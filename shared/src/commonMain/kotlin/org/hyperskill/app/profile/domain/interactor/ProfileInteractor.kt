package org.hyperskill.app.profile.domain.interactor

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

@Deprecated("ProfileInteractor is going to be removed. To access solvedStepsSharedFlow use SubmissionRepository directly.")
class ProfileInteractor(
    submissionRepository: SubmissionRepository
) {
    @Deprecated(
        "Use submissionRepository.solvedStepsMutableSharedFlow instead.",
        replaceWith = ReplaceWith(
            "submissionRepository.solvedStepsMutableSharedFlow",
            "import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository"
        )
    )
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow
}