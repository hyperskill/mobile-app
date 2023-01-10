package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import org.hyperskill.app.step_quiz.view.mapper.StepQuizUserPermissionRequestTextMapper
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment : Fragment(R.layout.fragment_step_quiz), ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {

    private lateinit var userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    private lateinit var viewStateDelegate: ViewStateDelegate<StepQuizFeature.State>
    private lateinit var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate
    private lateinit var stepQuizFormDelegate: StepQuizFormDelegate
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
        userPermissionRequestTextMapper = stepQuizComponent.stepQuizUserPermissionRequestTextMapper
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = StepQuizViewStateDelegateFactory.create(viewBinding, skeletonView, *quizViews)
        stepQuizFeedbackBlocksDelegate = StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate(viewBinding)

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
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ContinueClicked)
        }
        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    protected abstract fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate

    protected open fun onNewState(state: StepQuizFeature.State) {}

    protected fun onSubmitButtonClicked() {
        val reply = stepQuizFormDelegate.createReply()
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.CreateSubmissionClicked(step, reply))
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
            is StepQuizFeature.Action.ViewAction.NavigateTo.Back -> {
                requireRouter().exit()
            }
            is StepQuizFeature.Action.ViewAction.RequestUserPermission -> {
                when (action.userPermissionRequest) {
                    StepQuizUserPermissionRequest.RESET_CODE -> {
                        requestResetCodeActionPermission(action)
                    }
                    StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS -> {
                        requestSendDailyStudyRemindersPermission(action)
                    }
                }
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemOfDaySolvedModal -> {
                CompletedStepOfTheDayDialogFragment
                    .newInstance(gemsCount = action.gemsCount)
                    .showIfNotExists(childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
            }
        }
    }

    private fun requestResetCodeActionPermission(action: StepQuizFeature.Action.ViewAction.RequestUserPermission) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(userPermissionRequestTextMapper.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper.getMessage(action.userPermissionRequest))
            .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = true
                    )
                )
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = false
                    )
                )
            }
            .show()
    }

    private fun requestSendDailyStudyRemindersPermission(action: StepQuizFeature.Action.ViewAction.RequestUserPermission) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(userPermissionRequestTextMapper.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper.getMessage(action.userPermissionRequest))
            .setPositiveButton(org.hyperskill.app.R.string.ok) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = true
                    )
                )
                NotificationManagerCompat.from(requireContext())
                    .checkNotificationChannelAvailability(
                        requireContext(),
                        HyperskillNotificationChannel.DailyReminder
                    ) {
                        viewBinding.root.snackbar(org.hyperskill.app.R.string.common_error)
                    }
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = false
                    )
                )
            }
            .show()
    }

    override fun render(state: StepQuizFeature.State) {
        viewStateDelegate.switchState(state)

        val stepQuizButtonsLinearLayout = view?.findViewById<LinearLayout>(R.id.stepQuizButtons)
        if (stepQuizButtonsLinearLayout != null) {
            for (childView in stepQuizButtonsLinearLayout.children) {
                childView.isEnabled = !StepQuizResolver.isQuizLoading(state)
            }
        }

        if (state is StepQuizFeature.State.AttemptLoaded) {
            stepQuizFormDelegate.setState(state)
            stepQuizFeedbackBlocksDelegate.setState(stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state))
            viewBinding.stepQuizButtons.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

            if (state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                val castedState = state.submissionState as StepQuizFeature.SubmissionState.Loaded
                val submissionStatus = castedState.submission.status

                if (submissionStatus == SubmissionStatus.WRONG) {
                    if (step.block.name == BlockName.CODE || step.block.name == BlockName.SQL) {
                        setStepQuizButtonsState(StepQuizButtonsState.RETRY_LOGO_AND_SUBMIT)
                    } else {
                        setStepQuizButtonsState(
                            if (StepQuizResolver.isNeedRecreateAttemptForNewSubmission(step)) {
                                StepQuizButtonsState.RETRY
                            } else StepQuizButtonsState.SUBMIT
                        )
                    }
                } else if (submissionStatus == SubmissionStatus.CORRECT) {
                    setStepQuizButtonsState(
                        if (StepQuizResolver.isQuizRetriable(step)) {
                            StepQuizButtonsState.RETRY_LOGO_AND_CONTINUE
                        } else StepQuizButtonsState.CONTINUE
                    )
                } else {
                    setStepQuizButtonsState(StepQuizButtonsState.SUBMIT)
                }

                if (castedState.replyValidation is ReplyValidationResult.Error) {
                    val replyValidationError = castedState.replyValidation as ReplyValidationResult.Error
                    stepQuizFeedbackBlocksDelegate.setState(StepQuizFeedbackState.Validation(replyValidationError.message))
                }
            }
        }

        onNewState(state)
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

    private enum class StepQuizButtonsState {
        SUBMIT,
        RETRY,
        CONTINUE,
        RETRY_LOGO_AND_SUBMIT,
        RETRY_LOGO_AND_CONTINUE
    }

    // TODO: Refactor create custom StepQuizButtons class
    private fun setStepQuizButtonsState(state: StepQuizButtonsState) {
        when (state) {
            StepQuizButtonsState.SUBMIT -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.VISIBLE
                viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.GONE
            }
            StepQuizButtonsState.RETRY -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.VISIBLE
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.GONE
            }
            StepQuizButtonsState.CONTINUE -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.VISIBLE
            }
            StepQuizButtonsState.RETRY_LOGO_AND_SUBMIT -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.VISIBLE
                viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.visibility = View.VISIBLE
                viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.GONE
            }
            StepQuizButtonsState.RETRY_LOGO_AND_CONTINUE -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryButton.visibility = View.GONE
                viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.visibility = View.VISIBLE
                viewBinding.stepQuizButtons.stepQuizContinueButton.visibility = View.VISIBLE
            }
        }
    }
}