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

    private val viewStateDelegate = ViewStateDelegate<StepQuizFeedbackState>()

    init {
        with(viewStateDelegate) {
            addState<StepQuizFeedbackState.Idle>()
            addState<StepQuizFeedbackState.UnsupportedStep>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackUnsupported
            )
            addState<StepQuizFeedbackState.Evaluation>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackEvaluation
            )
            addState<StepQuizFeedbackState.Correct>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackCorrect,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedback
            )
            addState<StepQuizFeedbackState.Wrong>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackWrong,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedback
            )
            addState<StepQuizFeedbackState.ValidationFailed>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation
            )
            addState<StepQuizFeedbackState.RejectedSubmission>(
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation
            )
        }

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
            is StepQuizFeedbackState.Correct -> {
                setHint(layoutStepQuizFeedbackBlockBinding, state.hint, state.useLatex)
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

                    setHint(layoutStepQuizFeedbackBlockBinding, state.feedbackHint, state.useFeedbackHintLatex)
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
        layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding,
        hint: String?,
        useLatex: Boolean
    ) {
        val resultHint = hint?.let {
            if (useLatex) {
                hint
            } else {
                String.format(NON_LATEX_HINT_TEMPLATE, hint)
            }
        }
        layoutStepQuizFeedbackBlockBinding.stepQuizFeedback.isVisible = resultHint != null
        layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackBody.setText(resultHint)
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