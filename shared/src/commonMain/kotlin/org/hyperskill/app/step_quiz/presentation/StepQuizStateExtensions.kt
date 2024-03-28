package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.submissions.domain.model.Submission

val StepQuizFeature.StepQuizState.AttemptLoaded.submission: Submission?
    get() = (submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission