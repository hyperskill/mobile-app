package org.hyperskill.app.android.step_quiz.view.delegate

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.AnimationDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.LayoutStepQuizFeedbackBlockBinding
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step_quiz.view.model.StepQuizFeedbackState.Wrong.Action
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.getDrawableCompat

class StepQuizFeedbackBlocksDelegate(
    context: Context,
    private val layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding,
    private val onNewMessage: (StepQuizFeature.Message) -> Unit
) {
    companion object {
        private const val EVALUATION_FRAME_DURATION_MS = 250
        private const val NON_LATEX_HINT_TEMPLATE = """<pre><span style="font-family: 'Roboto';">%s</span></pre>"""
    }

    private val viewStateDelegate = ViewStateDelegate<StepQuizFeedbackState>().apply {
        addState<StepQuizFeedbackState.Idle>()
        addState<StepQuizFeedbackState.UnsupportedStep>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackUnsupported
        )
        addState<StepQuizFeedbackState.Evaluation>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackEvaluation,
            layoutStepQuizFeedbackBlockBinding.stepQuizCodeExecutionHint
        )
        addState<StepQuizFeedbackState.Correct>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackCorrect,
            layoutStepQuizFeedbackBlockBinding.stepQuizSubmissionHint,
            layoutStepQuizFeedbackBlockBinding.stepQuizCodeExecutionHint
        )
        addState<StepQuizFeedbackState.Wrong>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackWrong,
            layoutStepQuizFeedbackBlockBinding.stepQuizSubmissionHint
        )
        addState<StepQuizFeedbackState.ValidationFailed>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation
        )
        addState<StepQuizFeedbackState.RejectedSubmission>(
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation
        )
    }

    init {
        getEvaluationDrawable(context).let { evaluationDrawable ->
            layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackEvaluation
                .setCompoundDrawablesWithIntrinsicBounds(evaluationDrawable, null, null, null)
            evaluationDrawable.start()
        }

        with(layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackBody) {
            webViewClient =
                ProgressableWebViewClient(layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackProgress)
            textView?.typeface =
                ResourcesCompat.getFont(context, R.font.pt_mono)
        }

        with(layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackWrongAction) {
            paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

    fun setState(state: StepQuizFeedbackState) {
        viewStateDelegate.switchState(state)
        layoutStepQuizFeedbackBlockBinding.root.isVisible = state !is StepQuizFeedbackState.Idle
        when (state) {
            is StepQuizFeedbackState.Evaluation -> {
                setHint(state.hint, layoutStepQuizFeedbackBlockBinding)
            }
            is StepQuizFeedbackState.Correct -> {
                setHint(state.hint, layoutStepQuizFeedbackBlockBinding)
            }
            is StepQuizFeedbackState.Wrong -> {
                with(layoutStepQuizFeedbackBlockBinding) {
                    stepQuizFeedbackWrongTitle.text = state.title
                    stepQuizFeedbackWrongDescription.isVisible = state.description != null
                    if (state.description != null) {
                        stepQuizFeedbackWrongDescription.text = state.description
                    }

                    stepQuizFeedbackWrongAction.isVisible = state.actionText != null
                    if (state.actionText != null) {
                        stepQuizFeedbackWrongAction.text = state.actionText
                    }
                    val action = state.actionType
                    stepQuizFeedbackWrongAction.setOnClickListener(
                        if (action != null) {
                            {
                                onNewMessage(
                                    when (action) {
                                        Action.SEE_HINT -> StepQuizFeature.Message.SeeHintClicked
                                        Action.READ_COMMENTS -> StepQuizFeature.Message.ReadCommentsClicked
                                        Action.SKIP_PROBLEM -> StepQuizFeature.Message.SkipClicked
                                    }
                                )
                            }
                        } else null
                    )

                    setHint(state.hint, layoutStepQuizFeedbackBlockBinding)
                }
            }
            is StepQuizFeedbackState.ValidationFailed -> {
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation.text = state.message
            }
            is StepQuizFeedbackState.RejectedSubmission -> {
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation.text = state.message
            }
            else -> {
                // no op
            }
        }
    }

    private fun setHint(
        hint: StepQuizFeedbackState.Hint?,
        layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding
    ) {
        layoutStepQuizFeedbackBlockBinding.stepQuizSubmissionHint.isVisible =
            hint is StepQuizFeedbackState.Hint.FromSubmission
        layoutStepQuizFeedbackBlockBinding.stepQuizCodeExecutionHint.isVisible =
            hint is StepQuizFeedbackState.Hint.FromRunCodeExecution
        when (hint) {
            is StepQuizFeedbackState.Hint.FromSubmission ->
                setRemoteHint(hint, layoutStepQuizFeedbackBlockBinding)
            is StepQuizFeedbackState.Hint.FromRunCodeExecution ->
                setCodeExecutionHint(hint, layoutStepQuizFeedbackBlockBinding)
            null -> {
                // no op
            }
        }
    }

    private fun setRemoteHint(
        hint: StepQuizFeedbackState.Hint.FromSubmission,
        layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding
    ) {
        val resultHint = if (hint.useLatex) {
            hint.text
        } else {
            String.format(NON_LATEX_HINT_TEMPLATE, hint.text)
        }
        layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackBody.setText(resultHint)
    }

    private fun setCodeExecutionHint(
        hint: StepQuizFeedbackState.Hint.FromRunCodeExecution,
        layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding
    ) {
        with(layoutStepQuizFeedbackBlockBinding) {
            val isInputVisible =
                hint is StepQuizFeedbackState.Hint.FromRunCodeExecution.Result && hint.input != null
            stepQuizCodeExecutionInputTitleTextView.isVisible = isInputVisible
            stepQuizCodeExecutionInputValueTextView.isVisible = isInputVisible
            if (isInputVisible) {
                stepQuizCodeExecutionInputValueTextView.text =
                    (hint as? StepQuizFeedbackState.Hint.FromRunCodeExecution.Result)?.input
            }

            val isOutputVisible =
                hint is StepQuizFeedbackState.Hint.FromRunCodeExecution.Result
            stepQuizCodeExecutionOutputTitleTextView.isVisible = isOutputVisible
            stepQuizCodeExecutionOutputValueTextView.isVisible = isOutputVisible
            if (isOutputVisible) {
                stepQuizCodeExecutionOutputValueTextView.text =
                    (hint as? StepQuizFeedbackState.Hint.FromRunCodeExecution.Result)?.output
            }
        }
    }

    private fun getEvaluationDrawable(context: Context): AnimationDrawable =
        AnimationDrawable().apply {
            addFrame(
                context.getDrawableCompat(R.drawable.ic_step_quiz_evaluation_frame_1),
                EVALUATION_FRAME_DURATION_MS
            )
            addFrame(
                context.getDrawableCompat(R.drawable.ic_step_quiz_evaluation_frame_2),
                EVALUATION_FRAME_DURATION_MS
            )
            addFrame(
                context.getDrawableCompat(R.drawable.ic_step_quiz_evaluation_frame_3),
                EVALUATION_FRAME_DURATION_MS
            )
            isOneShot = false
        }
}