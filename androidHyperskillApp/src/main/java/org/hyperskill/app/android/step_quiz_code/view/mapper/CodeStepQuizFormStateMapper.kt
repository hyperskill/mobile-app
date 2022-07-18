package org.hyperskill.app.android.step_quiz_code.view.mapper

import org.hyperskill.app.android.step_quiz_code.view.model.CodeStepQuizFormState
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature

class CodeStepQuizFormStateMapper {
    fun mapToFormState(codeOptions: Block.Options, state: StepQuizFeature.State.AttemptLoaded): CodeStepQuizFormState {
        val (reply, lang) = resolveSubmissionState(state.submissionState)

        return when {
            lang != null ->
                CodeStepQuizFormState.Lang(lang, reply?.code ?: "")

            codeOptions.codeTemplates?.size == 1 ->
                codeOptions.codeTemplates!!.entries.first().let { (lang, code) -> CodeStepQuizFormState.Lang(lang, code) }

            else ->
                CodeStepQuizFormState.Idle
        }
    }

    private fun resolveSubmissionState(submissionState: StepQuizFeature.SubmissionState): Pair<Reply?, String?> =
        when (submissionState) {
            is StepQuizFeature.SubmissionState.Empty ->
                submissionState.reply to submissionState.reply?.language
            is StepQuizFeature.SubmissionState.Loaded ->
                submissionState.submission.reply to submissionState.submission.reply?.language
        }
}