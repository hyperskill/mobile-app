package org.hyperskill.app.android.step_quiz.view.delegate

import androidx.core.view.isVisible
import org.hyperskill.app.android.databinding.LayoutQuizButtonsBinding
import org.hyperskill.app.android.step_quiz.view.model.StepQuizButtonsState
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

class StepQuizButtonsDelegate(
    private val binding: LayoutQuizButtonsBinding,
    private val onSubmitButtonClicked: () -> Unit,
    private val onRetryButtonClicked: () -> Unit,
    private val onContinueClick: () -> Unit
) {

    init {
        binding.stepQuizSubmitButton.setOnClickListener {
            onSubmitButtonClicked()
        }
        binding.stepQuizRetryButton.setOnClickListener {
            onRetryButtonClicked()
        }
        binding.stepQuizRetryLogoOnlyButton.setOnClickListener {
            onRetryButtonClicked()
        }
        binding.stepQuizContinueButton.setOnClickListener {
            onContinueClick()
        }
    }

    private val stepQuizButtonsViewStateDelegate: ViewStateDelegate<StepQuizButtonsState> =
        ViewStateDelegate<StepQuizButtonsState>().apply {
            addState<StepQuizButtonsState.Submit>(binding.stepQuizSubmitButton)
            addState<StepQuizButtonsState.Retry>(binding.stepQuizRetryButton)
            addState<StepQuizButtonsState.Continue>(
                binding.stepQuizContinueButton,
                binding.stepQuizContinueFrame
            )
            addState<StepQuizButtonsState.RetryLogoAndSubmit>(
                binding.stepQuizRetryLogoOnlyButton,
                binding.stepQuizSubmitButton
            )
            addState<StepQuizButtonsState.RetryLogoAndContinue>(
                binding.stepQuizRetryLogoOnlyButton,
                binding.stepQuizContinueButton,
                binding.stepQuizContinueFrame
            )
        }

    fun render(state: StepQuizFeature.State.AttemptLoaded) {
        binding.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

        val areButtonsEnabled = !StepQuizResolver.isQuizLoading(state)
        binding.stepQuizRetryLogoOnlyButton.isEnabled = areButtonsEnabled
        binding.stepQuizContinueButton.isEnabled = areButtonsEnabled
        binding.stepQuizRetryButton.isEnabled = areButtonsEnabled

        val buttonsState = when (val submissionState = state.submissionState) {
            is StepQuizFeature.SubmissionState.Empty -> {
                StepQuizButtonsState.Submit
            }
            is StepQuizFeature.SubmissionState.Loaded -> {
                when (submissionState.submission.status) {
                    SubmissionStatus.WRONG -> when {
                        state.step.block.name == BlockName.CODE || state.step.block.name == BlockName.SQL -> {
                            StepQuizButtonsState.RetryLogoAndSubmit
                        }
                        StepQuizResolver.isNeedRecreateAttemptForNewSubmission(state.step) -> {
                            StepQuizButtonsState.Retry
                        }
                        else -> {
                            StepQuizButtonsState.Submit
                        }
                    }
                    SubmissionStatus.CORRECT -> {
                        if (StepQuizResolver.isQuizRetriable(state.step)) {
                            StepQuizButtonsState.RetryLogoAndContinue
                        } else {
                            StepQuizButtonsState.Continue
                        }
                    }
                    else -> StepQuizButtonsState.Submit
                }
            }
        }
        stepQuizButtonsViewStateDelegate.switchState(buttonsState)
    }

    fun renderPracticeLoading(isPracticingLoading: Boolean) {
        binding.stepQuizContinueButtonShimmer.isVisible = isPracticingLoading
        binding.stepQuizContinueButton.isEnabled = !isPracticingLoading
    }
}