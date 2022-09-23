package org.hyperskill.app.home.domain.interactor

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

class HomeInteractor(
    private val submissionRepository: SubmissionRepository
) {
    val solvedStepsSharedFlow: SharedFlow<Long> = submissionRepository.solvedStepsMutableSharedFlow
}