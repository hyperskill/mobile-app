package org.hyperskill.app.android.step_quiz.view.delegate

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ProgressableWebViewClient
import org.hyperskill.app.android.databinding.LayoutStepQuizFeedbackBlockBinding
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.getDrawableCompat

class StepQuizFeedbackBlocksDelegate(
    context: Context,
    private val layoutStepQuizFeedbackBlockBinding: LayoutStepQuizFeedbackBlockBinding
) {
    companion object {
        private const val EVALUATION_FRAME_DURATION_MS = 250
    }

    private val viewStateDelegate = ViewStateDelegate<StepQuizFeedbackState>()

    init {
        with(viewStateDelegate) {
            addState<StepQuizFeedbackState.Idle>()
            addState<StepQuizFeedbackState.Evaluation>(
                layoutStepQuizFeedbackBlockBinding.root,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackEvaluation
            )
            addState<StepQuizFeedbackState.Correct>(
                layoutStepQuizFeedbackBlockBinding.root,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackCorrect,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedback
            )
            addState<StepQuizFeedbackState.Wrong>(
                layoutStepQuizFeedbackBlockBinding.root,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackWrong,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedback
            )
            addState<StepQuizFeedbackState.Validation>(
                layoutStepQuizFeedbackBlockBinding.root,
                layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackValidation
            )
            addState<StepQuizFeedbackState.RejectedSubmission>(
                layoutStepQuizFeedbackBlockBinding.root,
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
                ProgressableWebViewClient(layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackProgress, webView)
            textView.typeface =
                ResourcesCompat.getFont(context, R.font.pt_mono)
        }
    }

    fun setState(state: StepQuizFeedbackState) {
        viewStateDelegate.switchState(state)
        when (state) {
            is StepQuizFeedbackState.Correct -> {
                setHint(layoutStepQuizFeedbackBlockBinding, state.hint)
            }
            is StepQuizFeedbackState.Wrong -> {
                setHint(layoutStepQuizFeedbackBlockBinding, state.hint)
            }
            is StepQuizFeedbackState.Validation -> {
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
        hint: String?
    ) {
        layoutStepQuizFeedbackBlockBinding.stepQuizFeedback.isVisible = hint != null
        layoutStepQuizFeedbackBlockBinding.stepQuizFeedbackBody.setText(hint)
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