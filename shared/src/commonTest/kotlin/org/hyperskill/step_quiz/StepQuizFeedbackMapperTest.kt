package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.submissions.domain.model.Feedback
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.commets.domain.model.stub
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz.domain.model.stub

class StepQuizFeedbackMapperTest {

    private val mapper = StepQuizFeedbackMapper(ResourceProviderStub())

    @Test
    fun `StepQuizState_Unsupported should be mapped to Unsupported viewState`() {
        val state = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.Unsupported,
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.State.Idle,
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.State.Idle
        )
        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.UnsupportedStep>(viewState)
    }

    @Test
    fun `All the StepQuizState except Unsupported and AttemptLoaded should be mapped to Idle viewState`() {
        listOf(
            StepQuizFeature.StepQuizState.Idle,
            StepQuizFeature.StepQuizState.Loading,
            StepQuizFeature.StepQuizState.NetworkError,
            StepQuizFeature.StepQuizState.AttemptLoading(
                StepQuizFeature.StepQuizState.AttemptLoaded(
                    step = Step.stub(id = 1),
                    attempt = Attempt.stub(),
                    submissionState = StepQuizFeature.SubmissionState.Empty(),
                    isProblemsLimitReached = false,
                    isTheoryAvailable = false
                )
            )
        ).forEach { stepQuizState ->
            val state = StepQuizFeature.State(
                stepQuizState = stepQuizState,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.State.Idle,
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.State.Idle
            )
            val viewState = mapper.map(state)

            assertIs<StepQuizFeedbackState.Idle>(viewState)
        }
    }

    @Test
    fun `ReplyValidationResult_Error should be mapped to ValidationFailed viewState`() {
        val replyValidationResultError = ReplyValidationResult.Error("Test message")

        val state = getAttemptLoadedSubmissionState(
            submission = Submission.stub(),
            replyValidation = replyValidationResultError
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.ValidationFailed>(viewState)
        assertEquals(replyValidationResultError.message, viewState.message)
    }

    @Test
    fun `SubmissionStatus_CORRECT should be mapped to Correct viewState`() {
        val hint = "Test hint"
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(
                status = SubmissionStatus.CORRECT,
                hint = hint
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Correct>(viewState)
        assertEquals(hint, viewState.hint)
    }

    @Test
    fun `SubmissionStatus_Evaluation should be mapped to Evaluation viewState`() {
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(status = SubmissionStatus.EVALUATION)
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Evaluation>(viewState)
    }

    @Test
    fun `SubmissionStatus_Local should be mapped to Idle viewState`() {
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(status = SubmissionStatus.LOCAL)
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Idle>(viewState)
    }

    @Test
    fun `SubmissionStatus_Rejected with feedback should be mapped to RejectedSubmission viewState`() {
        val feedback = Feedback.Text("Test feedback")
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(
                status = SubmissionStatus.REJECTED,
                feedback = feedback
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.RejectedSubmission>(viewState)
        assertEquals(feedback.text, viewState.message)
    }

    @Test
    fun `SubmissionStatus_Rejected without feedback should be mapped to Idle viewState`() {
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(
                status = SubmissionStatus.REJECTED
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Idle>(viewState)
    }

    @Test
    fun `SubmissionStatus_Wrong should be mapped to Wrong viewState`() {
        val state = getAttemptLoadedSubmissionState(
            Submission.stub(
                status = SubmissionStatus.WRONG
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Wrong>(viewState)
    }

    @Test
    fun `First wrong submission should trigger Wrong viewState with SEE_HINT actionType when hint is not opened`() {
        val stepId = 0L
        val state = getAttemptLoadedSubmissionState(
            step = Step.stub(id = stepId),
            submission = Submission.stub(
                status = SubmissionStatus.WRONG,
            ),
            wrongSubmissionCount = 1,
            stepQuizHintsState = StepQuizHintsFeature.State.Content(
                hintsIds = listOf(0, 1, 2),
                currentHint = null,
                hintHasReaction = false,
                areHintsLimited = false,
                stepId = stepId
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Wrong>(viewState)
        assertEquals(StepQuizFeedbackState.Wrong.Action.SEE_HINT, viewState.actionType)
    }

    /*ktlint-disable*/
    @Test
    fun `First wrong submission should trigger Wrong viewState with READ_COMMENTS actionType when hint is opened and there are comments`() {
        val stepId = 0L
        val state = getAttemptLoadedSubmissionState(
            step = Step.stub(
                id = stepId,
                commentsStatistics = listOf(
                    CommentStatisticsEntry(
                        thread = CommentThread.COMMENT,
                        totalCount = 1
                    )
                )
            ),
            submission = Submission.stub(
                status = SubmissionStatus.WRONG,
            ),
            wrongSubmissionCount = 1,
            stepQuizHintsState = StepQuizHintsFeature.State.Content(
                hintsIds = listOf(0, 1, 2),
                currentHint = Comment.stub(id = 0),
                hintHasReaction = false,
                areHintsLimited = false,
                stepId = stepId
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Wrong>(viewState)
        assertEquals(StepQuizFeedbackState.Wrong.Action.READ_COMMENTS, viewState.actionType)
    }

    /*ktlint-disable*/
    @Test
    fun `First wrong submission should trigger Wrong viewState with null actionType when hint is opened and no comments available`() {
        val stepId = 0L
        val state = getAttemptLoadedSubmissionState(
            step = Step.stub(
                id = stepId,
                commentsStatistics = emptyList()
            ),
            submission = Submission.stub(
                status = SubmissionStatus.WRONG,
            ),
            wrongSubmissionCount = 1,
            stepQuizHintsState = StepQuizHintsFeature.State.Content(
                hintsIds = listOf(0, 1, 2),
                currentHint = Comment.stub(id = 0),
                hintHasReaction = false,
                areHintsLimited = false,
                stepId = stepId
            )
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Wrong>(viewState)
        assertEquals(null, viewState.actionType)
    }

    @Test
    fun `Second wrong submission should trigger Wrong viewState with SKIP_PROBLEM action type`() {
        val stepId = 0L
        val state = getAttemptLoadedSubmissionState(
            step = Step.stub(id = stepId),
            submission = Submission.stub(
                status = SubmissionStatus.WRONG,
            ),
            wrongSubmissionCount = 2
        )

        val viewState = mapper.map(state)

        assertIs<StepQuizFeedbackState.Wrong>(viewState)
        assertEquals(StepQuizFeedbackState.Wrong.Action.SKIP_PROBLEM, viewState.actionType)
    }

    private fun getAttemptLoadedSubmissionState(
        submission: Submission,
        replyValidation: ReplyValidationResult? = null,
        wrongSubmissionCount: Int = 0,
        step: Step = Step.stub(id = 0),
        stepQuizHintsState: StepQuizHintsFeature.State = StepQuizHintsFeature.State.Idle
    ): StepQuizFeature.State =
        StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Loaded(
                    submission = submission,
                    replyValidation = replyValidation
                ),
                isProblemsLimitReached = false,
                isTheoryAvailable = false,
                wrongSubmissionsCount = wrongSubmissionCount
            ),
            stepQuizHintsState = stepQuizHintsState,
            stepQuizToolbarState = StepQuizToolbarFeature.State.Idle,
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.State.Idle
        )
}