package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment : Fragment(R.layout.fragment_step_quiz), ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {
    companion object {
        const val KEY_STEP = "key_step"
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    private lateinit var viewStateDelegate: ViewStateDelegate<StepQuizFeature.State>
    private lateinit var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate
    private lateinit var stepQuizFormDelegate: StepQuizFormDelegate
    private val stepQuizFeedbackMapper = StepQuizFeedbackMapper()

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View

    protected lateinit var step: Step

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        step = requireArguments().getParcelable<Step>(KEY_STEP) ?: throw IllegalStateException()
    }

    private fun injectComponent() {
        val stepQuizComponent = HyperskillApp.graph().buildStepQuizComponent()
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(stepQuizComponent)
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = StepQuizViewStateDelegateFactory.create(viewBinding, skeletonView, *quizViews)
        stepQuizFeedbackBlocksDelegate = StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate(viewBinding)

        viewBinding.stepQuizButtons.stepQuizSubmitButton.setOnClickListener {
            onActionButtonClicked()
        }
        viewBinding.stepQuizButtons.stepQuizRetryButton.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ClickedRetryEventMessage)
        }
        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ViewedEventMessage(step.id))
    }

    protected abstract fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate

    protected fun onActionButtonClicked() {
        val replyResult = stepQuizFormDelegate.createReply()
        when (replyResult.validation) {
            is ReplyResult.Validation.Success ->
                stepQuizViewModel.onNewMessage(StepQuizFeature.Message.CreateSubmissionClicked(step, replyResult.reply))

            is ReplyResult.Validation.Error ->
                stepQuizFeedbackBlocksDelegate.setState(StepQuizFeedbackState.Validation(replyResult.validation.message))
        }
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
                view?.snackbar(messageRes = R.string.connection_error)
            }
            is StepQuizFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                requireRouter().backTo(MainScreen)
            }
            is StepQuizFeature.Action.ViewAction.AskUserToEnableDailyReminders -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.after_daily_step_completed_dialog_title)
                    .setMessage(R.string.after_daily_step_completed_dialog_text)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.UserAgreedToEnableDailyReminders)
                    }
                    .setNegativeButton(R.string.later) { _, _ ->
                        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.UserDeclinedToEnableDailyReminders)
                    }
                    .show()
            }
        }
    }

    override fun render(state: StepQuizFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is StepQuizFeature.State.AttemptLoaded) {
            stepQuizFormDelegate.setState(state)
            stepQuizFeedbackBlocksDelegate.setState(stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state))
            viewBinding.stepQuizButtons.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

            if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                val castedState = state.submissionState as StepQuizFeature.SubmissionState.Loaded
                val submissionStatus = castedState.submission.status

                if (submissionStatus == SubmissionStatus.CORRECT) {
                    viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.GONE
                    viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.VISIBLE
                    viewBinding.stepQuizButtons.stepQuizContinueButton.setOnClickListener {
                        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ContinueClicked)
                    }
                }

                if (
                    step.block.name == BlockName.CODE &&
                    (submissionStatus == SubmissionStatus.CORRECT || submissionStatus == SubmissionStatus.WRONG)
                ) {
                    viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.VISIBLE
                }
            }
        }
    }

    protected fun syncReplyState(replyResult: ReplyResult) {
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.SyncReply(replyResult.reply))
    }

    /**
     * Use only for analytic events logging.
     * @param message an analytic event message
     */
    protected fun logAnalyticEventMessage(message: StepQuizFeature.Message) {
        stepQuizViewModel.onNewMessage(message)
    }
}