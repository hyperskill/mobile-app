package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment :
    Fragment(R.layout.fragment_step_quiz),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    StepCompletionView {

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    private var viewStateDelegate: ViewStateDelegate<StepQuizFeature.State>? = null
    private var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate? = null
    private var stepQuizFormDelegate: StepQuizFormDelegate? = null
    private var stepQuizButtonsViewStateDelegate: ViewStateDelegate<StepQuizButtonsState>? = null
    private val stepQuizFeedbackMapper = StepQuizFeedbackMapper()

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View

    protected var step: Step by argument(serializer = Step.serializer())
    protected var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepQuizComponent = HyperskillApp.graph().buildStepQuizComponent(stepRoute)
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(stepQuizComponent)
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = StepQuizViewStateDelegateFactory.create(viewBinding, skeletonView, *quizViews)
        stepQuizFeedbackBlocksDelegate = StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate(viewBinding).also { delegate ->
            delegate.customizeSubmissionButton(viewBinding.stepQuizButtons.stepQuizSubmitButton)
        }
        initButtonsViewStateDelegate()
        setupQuizButtons()

        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    private fun initButtonsViewStateDelegate() {
        stepQuizButtonsViewStateDelegate = ViewStateDelegate<StepQuizButtonsState>().apply {
            addState<StepQuizButtonsState.Submit>(viewBinding.stepQuizButtons.stepQuizSubmitButton)
            addState<StepQuizButtonsState.Retry>(viewBinding.stepQuizButtons.stepQuizRetryButton)
            addState<StepQuizButtonsState.Continue>(
                viewBinding.stepQuizButtons.stepQuizContinueButton,
                viewBinding.stepQuizButtons.stepQuizContinueFrame
            )
            addState<StepQuizButtonsState.RetryLogoAndSubmit>(
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton,
                viewBinding.stepQuizButtons.stepQuizSubmitButton
            )
            addState<StepQuizButtonsState.RetryLogoAndContinue>(
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton,
                viewBinding.stepQuizButtons.stepQuizContinueButton,
                viewBinding.stepQuizButtons.stepQuizContinueFrame
            )
        }
    }

    private fun setupQuizButtons() {
        viewBinding.stepQuizButtons.stepQuizSubmitButton.setOnClickListener {
            onSubmitButtonClicked()
        }
        viewBinding.stepQuizButtons.stepQuizRetryButton.setOnClickListener {
            onRetryButtonClicked()
        }
        viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.setOnClickListener {
            onRetryButtonClicked()
        }
        viewBinding.stepQuizButtons.stepQuizContinueButton.setOnClickListener {
            parentOfType(StepCompletionHost::class.java)
                ?.onNewMessage(StepCompletionFeature.Message.ContinuePracticingClicked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
        stepQuizButtonsViewStateDelegate = null
        stepQuizFeedbackBlocksDelegate = null
        stepQuizFormDelegate = null
    }

    protected abstract fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate

    protected open fun onNewState(state: StepQuizFeature.State) {}

    protected fun onSubmitButtonClicked() {
        stepQuizFormDelegate?.createReply()?.let { reply ->
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.CreateSubmissionClicked(step, reply))
        }
    }

    protected fun onRetryButtonClicked() {
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ClickedRetryEventMessage)
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.CreateAttemptClicked(
                step = step,
                shouldResetReply = true
            )
        )
    }

    override fun onStart() {
        super.onStart()
        stepQuizViewModel.attachView(this)
    }

    override fun onStop() {
        stepQuizViewModel.detachView(this)
        super.onStop()
    }

    override fun onAction(action: StepQuizFeature.Action.ViewAction) {
        when (action) {
            is StepQuizFeature.Action.ViewAction.ShowNetworkError -> {
                view?.snackbar(messageRes = org.hyperskill.app.R.string.connection_error)
            }
            is StepQuizFeature.Action.ViewAction.RequestResetCode -> {
                requestResetCodeActionPermission()
            }
        }
    }

    private fun requestResetCodeActionPermission() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(SharedResources.strings.reset_code_dialog_title.resourceId)
            .setMessage(SharedResources.strings.reset_code_dialog_explanation.resourceId)
            .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestResetCodeResult(isGranted = true)
                )
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel) { _, _ ->
                StepQuizFeature.Message.RequestResetCodeResult(isGranted = false)
            }
            .show()
    }

    override fun render(state: StepQuizFeature.State) {
        viewStateDelegate?.switchState(state)

        val stepQuizButtonsLinearLayout = view?.findViewById<LinearLayout>(R.id.stepQuizButtons)
        if (stepQuizButtonsLinearLayout != null) {
            for (childView in stepQuizButtonsLinearLayout.children) {
                childView.isEnabled = !StepQuizResolver.isQuizLoading(state)
            }
        }

        if (state is StepQuizFeature.State.AttemptLoaded) {
            stepQuizFormDelegate?.setState(state)
            stepQuizFeedbackBlocksDelegate?.setState(stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state))
            viewBinding.stepQuizButtons.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

            when (val submissionState = state.submissionState) {
                is StepQuizFeature.SubmissionState.Loaded -> {
                    val buttonsState = when (submissionState.submission.status) {
                        SubmissionStatus.WRONG -> when {
                            step.block.name == BlockName.CODE || step.block.name == BlockName.SQL ->
                                StepQuizButtonsState.RetryLogoAndSubmit
                            StepQuizResolver.isNeedRecreateAttemptForNewSubmission(step) ->
                                StepQuizButtonsState.Retry
                            else -> StepQuizButtonsState.Submit
                        }
                        SubmissionStatus.CORRECT -> {
                            if (StepQuizResolver.isQuizRetriable(step)) {
                                StepQuizButtonsState.RetryLogoAndContinue
                            } else {
                                StepQuizButtonsState.Continue
                            }
                        }
                        else -> StepQuizButtonsState.Submit
                    }
                    stepQuizButtonsViewStateDelegate?.switchState(buttonsState)

                    val replyValidation = submissionState.replyValidation
                    if (replyValidation is ReplyValidationResult.Error) {
                        stepQuizFeedbackBlocksDelegate?.setState(StepQuizFeedbackState.Validation(replyValidation.message))
                    }
                }
                is StepQuizFeature.SubmissionState.Empty -> {
                    stepQuizButtonsViewStateDelegate?.switchState(StepQuizButtonsState.Submit)
                }
            }
        }

        onNewState(state)
    }

    final override fun render(isPracticingLoading: Boolean) {
        if (isResumed) {
            with(viewBinding) {
                stepQuizButtons.stepQuizContinueButtonShimmer.isVisible = isPracticingLoading
                stepQuizButtons.stepQuizContinueButton.isEnabled = !isPracticingLoading
            }
        }
    }

    protected fun syncReplyState(reply: Reply) {
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.SyncReply(reply))
    }

    /**
     * Use only for analytic events logging.
     * @param message an analytic event message
     */
    protected fun logAnalyticEventMessage(message: StepQuizFeature.Message) {
        stepQuizViewModel.onNewMessage(message)
    }

    private sealed interface StepQuizButtonsState {
        object Submit : StepQuizButtonsState
        object Retry : StepQuizButtonsState
        object Continue : StepQuizButtonsState
        object RetryLogoAndSubmit : StepQuizButtonsState
        object RetryLogoAndContinue : StepQuizButtonsState
    }
}