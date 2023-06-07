package org.hyperskill.step_quiz.domain.model

import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.attempts.AttemptStatus
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset

// TODO: make a stub of the attempt for concrete step (choice, code, etc.)

fun Attempt.Companion.stub(
    id: Long = 0L,
    dataset: Dataset? = null,
    status: AttemptStatus? = null
): Attempt =
    Attempt(
        id = id,
        dataset = dataset,
        status = status
    )