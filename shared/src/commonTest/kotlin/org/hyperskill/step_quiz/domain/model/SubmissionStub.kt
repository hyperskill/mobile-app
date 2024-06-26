package org.hyperskill.step_quiz.domain.model

import org.hyperskill.app.submissions.domain.model.Feedback
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus

fun Submission.Companion.stub(
    id: Long = 0L,
    status: SubmissionStatus? = null,
    originalStatus: SubmissionStatus? = null,
    score: String? = null,
    hint: String? = null,
    time: String? = null,
    reply: Reply? = null,
    attempt: Long = 0L,
    feedback: Feedback? = null
): Submission =
    Submission(
        id = id,
        status = status,
        originalStatus = originalStatus,
        score = score,
        hint = hint,
        time = time,
        reply = reply,
        attempt = attempt,
        feedback = feedback
    )