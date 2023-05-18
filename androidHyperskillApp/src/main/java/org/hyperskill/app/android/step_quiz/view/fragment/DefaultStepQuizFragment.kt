package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.problems_limit.dialog.ProblemsLimitReachedBottomSheet
import org.hyperskill.app.android.problems_limit.view.ui.delegate.ProblemsLimitDelegate
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.android.step_quiz_hints.fragment.StepQuizHintsFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizUserPermissionRequestTextMapper
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment :
    Fragment(R.layout.fragment_step_quiz),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    StepCompletionView {

    companion object {
        private const val STEP_HINTS_FRAGMENT_TAG = "step_hints"
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)

    private var stepQuizStateDelegate: ViewStateDelegate<StepQuizFeature.StepQuizState>? = null

    private var problemsLimitDelegate: ProblemsLimitDelegate? = null
    private var problemsLimitViewStateMapper: ProblemsLimitViewStateMapper? = null

    private var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate? = null
    private var stepQuizFormDelegate: StepQuizFormDelegate? = null
    private var stepQuizButtonsViewStateDelegate: ViewStateDelegate<StepQuizButtonsState>? = null

    private var userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper? = null
    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private var stepQuizTitleMapper: StepQuizTitleMapper? = null
    private val stepQuizFeedbackMapper by lazy(LazyThreadSafetyMode.NONE) {
        StepQuizFeedbackMapper()
    }

    private val platformNotificationComponent =
        HyperskillApp.graph().platformNotificationComponent

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View
    protected abstract val descriptionBinding: LayoutStepQuizDescriptionBinding

    protected var step: Step by argument(serializer = Step.serializer())
    protected var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepQuizComponent = HyperskillApp.graph().buildStepQuizComponent(stepRoute)
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(stepQuizComponent)
        val problemsLimitComponent = HyperskillApp.graph().buildProblemsLimitComponent(ProblemsLimitScreen.STEP_QUIZ)
        userPermissionRequestTextMapper = stepQuizComponent.stepQuizUserPermissionRequestTextMapper
        stepQuizStatsTextMapper = stepQuizComponent.stepQuizStatsTextMapper
        stepQuizTitleMapper = stepQuizComponent.stepQuizTitleMapper
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
        problemsLimitViewStateMapper = problemsLimitComponent.problemsLimitViewStateMapper
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stepView = createStepView(LayoutInflater.from(requireContext()), viewBinding.root)
        viewBinding.root.addView(stepView)

        initProblemsLimitDelegate()

        stepQuizStateDelegate = StepQuizViewStateDelegateFactory.create(
            fragmentStepQuizBinding = viewBinding,
            descriptionBinding = descriptionBinding,
            skeletonView = skeletonView,
            quizViews = quizViews
        )
        stepQuizFeedbackBlocksDelegate =
            StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate().also { delegate ->
            delegate.customizeSubmissionButton(viewBinding.stepQuizButtons.stepQuizSubmitButton)
        }
        renderStatistics(viewBinding.stepQuizStatistics, step)
        initButtonsViewStateDelegate()
        setupQuizButtons()

        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    private fun initProblemsLimitDelegate() {
        problemsLimitDelegate = ProblemsLimitDelegate(
            viewBinding = viewBinding.stepQuizProblemsLimit,
            onNewMessage = {
                stepQuizViewModel.onNewMessage(StepQuizFeature.Message.ProblemsLimitMessage(it))
            }
        )
        problemsLimitDelegate?.setup()
    }

    private fun renderStatistics(textView: TextView, step: Step) {
        val stepQuizStats =
            stepQuizStatsTextMapper?.getFormattedStepQuizStats(step.solvedBy, step.millisSinceLastCompleted)
        textView.text = stepQuizStats
        textView.isVisible = stepQuizStats != null
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
        stepQuizStateDelegate = null
        stepQuizButtonsViewStateDelegate = null
        stepQuizFeedbackBlocksDelegate = null
        stepQuizFormDelegate = null
        problemsLimitDelegate?.cleanup()
        problemsLimitDelegate = null
    }

    protected abstract fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View

    protected abstract fun createStepQuizFormDelegate(): StepQuizFormDelegate

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
            is StepQuizFeature.Action.ViewAction.NavigateTo.Back -> {
                requireRouter().exit()
            }
            is StepQuizFeature.Action.ViewAction.NavigateTo.Home -> {
                requireRouter().backTo(MainScreen)
                mainScreenRouter.switch(HomeScreen)
            }
            is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen -> {
                requireRouter().navigateTo(StepScreen(action.stepRoute))
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
            StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal -> {
                ProblemsLimitReachedBottomSheet.newInstance()
                    .showIfNotExists(childFragmentManager, ProblemsLimitReachedBottomSheet.TAG)
            }
            is StepQuizFeature.Action.ViewAction.ProblemsLimitViewAction -> {}
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

    private fun requestSendDailyStudyRemindersPermission(
        action: StepQuizFeature.Action.ViewAction.RequestUserPermission
    ) {
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
        stepQuizStateDelegate?.switchState(state.stepQuizState)

        val stepQuizButtonsLinearLayout = view?.findViewById<LinearLayout>(R.id.stepQuizButtons)
        if (stepQuizButtonsLinearLayout != null) {
            for (childView in stepQuizButtonsLinearLayout.children) {
                childView.isEnabled = !StepQuizResolver.isQuizLoading(state.stepQuizState)
            }
        }

        when (val stepQuizState = state.stepQuizState) {
            StepQuizFeature.StepQuizState.Unsupported -> {
                stepQuizFeedbackBlocksDelegate?.setState(StepQuizFeedbackState.Unsupported)
            }
            is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                setStepHintsFragment(step)
                renderAttemptLoaded(stepQuizState)

                problemsLimitViewStateMapper?.let { mapper ->
                    val problemsLimitViewState = mapper.mapState(state.problemsLimitState)
                    viewBinding.problemsLimitDivider.root.isVisible =
                        problemsLimitViewState is ProblemsLimitFeature.ViewState.Content.Widget
                    problemsLimitDelegate?.render(problemsLimitViewState)
                }
            }
            else -> {
                // no op
            }
        }

        val stepQuizToolbar = parentFragment?.view?.findViewById<MaterialToolbar>(R.id.stepQuizToolbar)
        stepQuizToolbar?.menu?.findItem(R.id.theory)?.apply {
            isVisible = when (state.stepQuizState) {
                is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                    (state.stepQuizState as StepQuizFeature.StepQuizState.AttemptLoaded).isTheoryAvailable
                }
                is StepQuizFeature.StepQuizState.AttemptLoading -> {
                    (state.stepQuizState as StepQuizFeature.StepQuizState.AttemptLoading).oldState.isTheoryAvailable
                }
                else -> false
            }
            // TODO: isEnabled not working
            isEnabled = !StepQuizResolver.isQuizLoading(state.stepQuizState)
            actionView?.setOnClickListener {
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.TheoryToolbarClicked
                )
            }
        }

        onNewState(state)
    }

    private fun renderAttemptLoaded(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        descriptionBinding.stepQuizDescription.text =
            stepQuizTitleMapper?.getStepQuizTitle(
                blockName = step.block.name,
                isMultipleChoice = state.attempt.dataset?.isMultipleChoice,
                isCheckbox = state.attempt.dataset?.isCheckbox
            )
        stepQuizFormDelegate?.setState(state)
        stepQuizFeedbackBlocksDelegate?.setState(
            stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state)
        )
        viewBinding.stepQuizButtons.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

        when (val submissionState = state.submissionState) {
            is StepQuizFeature.SubmissionState.Loaded -> {
                val buttonsState = when (submissionState.submission.status) {
                    SubmissionStatus.WRONG, SubmissionStatus.REJECTED -> when {
                        step.block.name in BlockName.codeRelatedBlocksNames ->
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
                    stepQuizFeedbackBlocksDelegate?.setState(
                        StepQuizFeedbackState.Validation(replyValidation.message)
                    )
                }
            }
            is StepQuizFeature.SubmissionState.Empty -> {
                stepQuizButtonsViewStateDelegate?.switchState(StepQuizButtonsState.Submit)
            }
        }
    }

    private fun setStepHintsFragment(step: Step) {
        val isFeatureEnabled = StepQuizHintsFeature.isHintsFeatureAvailable(step)
        viewBinding.stepQuizHints.isVisible = isFeatureEnabled
        if (isFeatureEnabled) {
            setChildFragment(R.id.stepQuizHints, STEP_HINTS_FRAGMENT_TAG) {
                StepQuizHintsFragment.newInstance(stepRoute, step)
            }
        }
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