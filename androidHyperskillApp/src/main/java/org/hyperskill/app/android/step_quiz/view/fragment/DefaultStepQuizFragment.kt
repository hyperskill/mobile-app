package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizButtonsDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizUserPermissionRequestTextMapper
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment :
    Fragment(R.layout.fragment_step_quiz),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    StepCompletionView {

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)

    private var viewStateDelegate: ViewStateDelegate<StepQuizFeature.State>? = null

    private var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate? = null
    private var stepQuizFormDelegate: StepQuizFormDelegate? = null
    private var stepQuizButtonsDelegate: StepQuizButtonsDelegate? = null

    private var userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper? = null
    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private var stepQuizTitleMapper: StepQuizTitleMapper? = null
    private val stepQuizFeedbackMapper by lazy(LazyThreadSafetyMode.NONE) {
        StepQuizFeedbackMapper()
    }

    private val platformNotificationComponent =
        HyperskillApp.graph().platformNotificationComponent

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
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(step, stepQuizComponent)
        userPermissionRequestTextMapper = stepQuizComponent.stepQuizUserPermissionRequestTextMapper
        stepQuizStatsTextMapper = stepQuizComponent.stepQuizStatsTextMapper
        stepQuizTitleMapper = stepQuizComponent.stepQuizTitleMapper
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = StepQuizViewStateDelegateFactory.create(viewBinding, skeletonView, *quizViews)
        stepQuizFeedbackBlocksDelegate = StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate().also { delegate ->
            delegate.customizeSubmissionButton(viewBinding.stepQuizButtons.stepQuizSubmitButton)
        }
        renderStatistics(viewBinding.stepQuizStatistics, step)
        initStepQuizButtonsDelegate()

        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }
    }

    private fun renderStatistics(textView: TextView, step: Step) {
        val stepQuizStats =
            stepQuizStatsTextMapper?.getFormattedStepQuizStats(step.solvedBy, step.millisSinceLastCompleted)
        textView.text = stepQuizStats
        textView.isVisible = stepQuizStats != null
    }

    private fun initStepQuizButtonsDelegate() {
        stepQuizButtonsDelegate = StepQuizButtonsDelegate(
            binding = viewBinding.stepQuizButtons,
            onSubmitButtonClicked = ::onSubmitButtonClicked,
            onRetryButtonClicked = ::onRetryButtonClicked,
            onContinueClick = {
                parentOfType(StepCompletionHost::class.java)
                    ?.onNewMessage(StepCompletionFeature.Message.ContinuePracticingClicked)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
        stepQuizButtonsDelegate = null
        stepQuizFeedbackBlocksDelegate = null
        stepQuizFormDelegate = null
    }

    protected abstract fun createStepQuizFormDelegate(): StepQuizFormDelegate

    protected open fun onNewState(state: StepQuizFeature.State) {}

    protected fun onSubmitButtonClicked() {
        stepQuizFormDelegate?.createReply()?.let { reply ->
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.CreateSubmissionClicked(step, reply))
        }
    }

    protected fun onRetryButtonClicked() {
        stepQuizViewModel.onRetryButtonClicked()
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
                    .newInstance(earnedGemsText = action.earnedGemsText)
                    .showIfNotExists(childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
            }
        }
    }

    private fun requestResetCodeActionPermission(action: StepQuizFeature.Action.ViewAction.RequestUserPermission) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(userPermissionRequestTextMapper?.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper?.getMessage(action.userPermissionRequest))
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
            .setTitle(userPermissionRequestTextMapper?.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper?.getMessage(action.userPermissionRequest))
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                onSendDailyStudyReminderAccepted(action.userPermissionRequest)
                dialog.dismiss()
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = false
                    )
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun onSendDailyStudyReminderAccepted(userPermissionRequest: StepQuizUserPermissionRequest) {
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.RequestUserPermissionResult(
                userPermissionRequest,
                isGranted = true
            )
        )
        NotificationManagerCompat.from(requireContext()).checkNotificationChannelAvailability(
            requireContext(),
            HyperskillNotificationChannel.DailyReminder
        ) {
            if (isResumed) {
                viewBinding.root.snackbar(org.hyperskill.app.R.string.common_error)
            }
        }
        platformNotificationComponent.dailyStudyReminderNotificationDelegate.scheduleDailyNotification()
    }

    override fun render(state: StepQuizFeature.State) {
        viewStateDelegate?.switchState(state)

        if (state is StepQuizFeature.State.AttemptLoaded) {
            viewBinding.stepQuizDescription.text =
                stepQuizTitleMapper?.getStepQuizTitle(
                    blockName = step.block.name,
                    isMultipleChoice = state.attempt.dataset?.isMultipleChoice,
                    isCheckbox = state.attempt.dataset?.isCheckbox
                )
            stepQuizFormDelegate?.setState(state)
            stepQuizFeedbackBlocksDelegate?.setState(stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state))
            stepQuizButtonsDelegate?.render(state)
            renderFeedback(state)
        }

        onNewState(state)
    }

    private fun renderFeedback(state: StepQuizFeature.State.AttemptLoaded) {
        val submissionState = state.submissionState
        if (submissionState is StepQuizFeature.SubmissionState.Loaded) {
            val replyValidation = submissionState.replyValidation
            if (replyValidation is ReplyValidationResult.Error) {
                stepQuizFeedbackBlocksDelegate?.setState(
                    StepQuizFeedbackState.Validation(replyValidation.message)
                )
            }
        }
    }

    final override fun render(isPracticingLoading: Boolean) {
        if (isResumed) {
            stepQuizButtonsDelegate?.renderPracticeLoading(isPracticingLoading)
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
}