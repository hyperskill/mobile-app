package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.repository.SubmissionRepository

interface SubmissionDataComponent {
    val submissionRepository: SubmissionRepository
}