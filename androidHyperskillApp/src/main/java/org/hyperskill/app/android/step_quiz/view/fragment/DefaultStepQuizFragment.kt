package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chrisbanes.insetter.applyInsetter
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.performConfirmHapticFeedback
import org.hyperskill.app.android.core.extensions.performRejectHapticFeedback
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.problems_limit.dialog.ProblemsLimitInfoBottomSheet
import org.hyperskill.app.android.step.view.model.LimitsWidgetCallback
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.model.StepHost
import org.hyperskill.app.android.step.view.model.StepMenuPrimaryAction
import org.hyperskill.app.android.step.view.model.StepMenuPrimaryActionParams
import org.hyperskill.app.android.step.view.model.StepPracticeCallback
import org.hyperskill.app.android.step.view.model.StepToolbarCallback
import org.hyperskill.app.android.step.view.model.StepToolbarContentViewState
import org.hyperskill.app.android.step.view.model.StepToolbarHost
import org.hyperskill.app.android.step_practice.model.StepPracticeHost
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.dialog.ProblemOnboardingBottomSheetCallback
import org.hyperskill.app.android.step_quiz.view.dialog.ProblemsOnboardingBottomSheetFactory
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.model.StepQuizButtonsState
import org.hyperskill.app.android.step_quiz_hints.delegate.StepQuizHintsDelegate
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.mapper.StepQuizHintsViewStateMapper
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.step_quiz_toolbar.view.StepQuizToolbarViewStateMapper
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment :
    Fragment(R.layout.fragment_step_quiz),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    StepCompletionView,
    ProblemOnboardingBottomSheetCallback,
    LimitsWidgetCallback,
    StepPracticeCallback {

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)

    private var stepQuizStateDelegate: ViewStateDelegate<StepQuizFeature.StepQuizState>? = null

    private var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate? = null
    private var stepQuizFormDelegate: StepQuizFormDelegate? = null
    private var stepQuizButtonsViewStateDelegate: ViewStateDelegate<StepQuizButtonsState>? = null

    private var stepQuizHintsDelegate: StepQuizHintsDelegate? = null

    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private var stepQuizTitleMapper: StepQuizTitleMapper? = null
    private var stepQuizFeedbackMapper: org.hyperskill.app.step_quiz.view.mapper.StepQuizFeedbackMapper? = null

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View
    protected abstract val descriptionBinding: LayoutStepQuizDescriptionBinding?

    protected var step: Step by argument(serializer = Step.serializer())
    protected var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    private var isKeyboardShown: Boolean = false

    private val svgImageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepQuizComponent = HyperskillApp.graph().buildStepQuizComponent(stepRoute)
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(stepQuizComponent)
        stepQuizStatsTextMapper = stepQuizComponent.stepQuizStatsTextMapper
        stepQuizTitleMapper = stepQuizComponent.stepQuizTitleMapper
        stepQuizFeedbackMapper = stepQuizComponent.stepQuizFeedbackMapper
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyWindowInsets()

        val stepView = createStepView(LayoutInflater.from(requireContext()), viewBinding.root)
        viewBinding.root.addView(stepView)

        stepQuizStateDelegate = StepQuizViewStateDelegateFactory.create(
            fragmentStepQuizBinding = viewBinding,
            descriptionBinding = descriptionBinding,
            skeletonView = skeletonView,
            quizViews = quizViews
        )
        viewBinding.stepQuizFeedbackBlocks.stepQuizFeedbackWrong.isHapticFeedbackEnabled = true
        stepQuizFeedbackBlocksDelegate =
            StepQuizFeedbackBlocksDelegate(
                context = requireContext(),
                layoutStepQuizFeedbackBlockBinding = viewBinding.stepQuizFeedbackBlocks,
                onNewMessage = stepQuizViewModel::onNewMessage
            )
        stepQuizFormDelegate = createStepQuizFormDelegate().also { delegate ->
            delegate.customizeSubmissionButton(viewBinding.stepQuizButtons.stepQuizSubmitButton)
        }
        stepQuizHintsDelegate = StepQuizHintsDelegate(
            binding = viewBinding.stepQuizHints,
            imageLoader = svgImageLoader,
            onNewMessage = { message ->
                stepQuizViewModel.onNewMessage(StepQuizFeature.Message.StepQuizHintsMessage(message))
            }
        )
        renderStatistics(viewBinding.stepQuizStatistics, step)
        initButtonsViewStateDelegate()
        setupQuizButtons()

        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    private fun applyWindowInsets() {
        viewBinding.stepQuizButtons.root.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }
    }

    private fun renderStatistics(textView: TextView, step: Step) {
        val stepQuizStats =
            stepQuizStatsTextMapper?.getFormattedStepQuizStats(step.solvedBy, step.millisSinceLastCompleted)
        textView.text = stepQuizStats
        textView.isVisible = stepQuizStats != null && !isKeyboardShown
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
        with(viewBinding.stepQuizButtons.stepQuizSubmitButton) {
            isHapticFeedbackEnabled = true
            setOnClickListener {
                onSubmitButtonClicked()
            }
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
        stepQuizStateDelegate = null
        stepQuizButtonsViewStateDelegate = null
        stepQuizFeedbackBlocksDelegate = null
        stepQuizFormDelegate = null
        stepQuizHintsDelegate = null
        super.onDestroyView()
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
            is StepQuizFeature.Action.ViewAction.NavigateTo.TheoryStepScreen -> {
                parentOfType(StepHost::class.java)?.navigateToTheory(action.stepRoute)
            }
            is StepQuizFeature.Action.ViewAction.RequestResetCode -> {
                requestResetCodeActionPermission()
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal -> {
                showProblemsLimitInfoBottomSheet(
                    ProblemsLimitInfoModalFeatureParams(
                        subscription = action.subscription,
                        chargeLimitsStrategy = action.chargeLimitsStrategy,
                        context = action.context
                    )
                )
            }
            StepQuizFeature.Action.ViewAction.HideProblemsLimitReachedModal -> {
                childFragmentManager.dismissDialogFragmentIfExists(ProblemsLimitInfoBottomSheet.TAG)
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemOnboardingModal -> {
                ProblemsOnboardingBottomSheetFactory
                    .getProblemsOnboardingBottomSheet(action.modalType)
                    .showIfNotExists(
                        childFragmentManager,
                        ProblemsOnboardingBottomSheetFactory.TAG
                    )
            }
            is StepQuizFeature.Action.ViewAction.StepQuizHintsViewAction -> {
                stepQuizHintsDelegate?.onAction(action.viewAction)
            }
            StepQuizFeature.Action.ViewAction.NavigateTo.StudyPlan -> {
                // TODO: ALTAPPS-807
            }
            is StepQuizFeature.Action.ViewAction.CreateMagicLinkState -> {
                // TODO: ALTAPPS-807
            }
            is StepQuizFeature.Action.ViewAction.OpenUrl -> {
                // TODO: ALTAPPS-807
            }
            is StepQuizFeature.Action.ViewAction.StepQuizToolbarViewAction -> {
                when (val viewAction = action.viewAction) {
                    is StepQuizToolbarFeature.Action.ViewAction.ShowProblemsLimitInfoModal -> {
                        showProblemsLimitInfoBottomSheet(
                            ProblemsLimitInfoModalFeatureParams(
                                subscription = viewAction.subscription,
                                chargeLimitsStrategy = viewAction.chargeLimitsStrategy,
                                context = viewAction.context
                            )
                        )
                    }
                }
            }
            StepQuizFeature.Action.ViewAction.HapticFeedback.CorrectSubmission -> {
                viewBinding.stepQuizButtons.stepQuizContinueButton.performConfirmHapticFeedback()
            }
            StepQuizFeature.Action.ViewAction.HapticFeedback.WrongSubmission,
            StepQuizFeature.Action.ViewAction.HapticFeedback.ReplyValidationError -> {
                viewBinding.stepQuizButtons.stepQuizSubmitButton.performRejectHapticFeedback()
            }
            StepQuizFeature.Action.ViewAction.ScrollToCallToActionButton -> {
                handleScrollToCallToActionButton()
            }
            StepQuizFeature.Action.ViewAction.ScrollToHints -> {
                parentOfType(StepPracticeHost::class.java)
                    ?.scrollTo(viewBinding.stepQuizHints.root)
            }
            is StepQuizFeature.Action.ViewAction.RequestShowComments -> {
                parentOfType(StepToolbarCallback::class.java)
                    ?.requestShowComments()
            }
            is StepQuizFeature.Action.ViewAction.RequestSkipStep -> {
                parentOfType(StepToolbarCallback::class.java)
                    ?.requestSkip()
            }
            is StepQuizFeature.Action.ViewAction.StepQuizCodeBlanksViewAction -> {
                // no op
            }
        }
    }

    private fun showProblemsLimitInfoBottomSheet(
        params: ProblemsLimitInfoModalFeatureParams
    ) {
        ProblemsLimitInfoBottomSheet
            .newInstance(params)
            .showIfNotExists(childFragmentManager, ProblemsLimitInfoBottomSheet.TAG)
    }

    private fun requestResetCodeActionPermission() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.hyperskill.app.R.string.reset_code_dialog_title)
            .setMessage(org.hyperskill.app.R.string.reset_code_dialog_explanation)
            .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestResetCodeResult(isGranted = true)
                )
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel) { _, _ ->
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.RequestResetCodeResult(isGranted = false)
                )
            }
            .show()
    }

    private fun handleScrollToCallToActionButton() {
        requireView().post {
            viewBinding
                .stepQuizButtons
                .root
                .children
                .firstOrNull { it.isVisible }
                ?.doOnLayout {
                    if (isResumed) {
                        parentOfType(StepPracticeHost::class.java)?.fullScrollDown()
                    }
                }
        }
    }

    override fun render(state: StepQuizFeature.State) {
        renderLimits(state.stepQuizToolbarState)
        stepQuizStateDelegate?.switchState(state.stepQuizState)

        updateStatisticsVisibility()

        val stepQuizButtonsLinearLayout = view?.findViewById<LinearLayout>(R.id.stepQuizButtons)
        if (stepQuizButtonsLinearLayout != null) {
            for (childView in stepQuizButtonsLinearLayout.children) {
                childView.isEnabled = !StepQuizResolver.isQuizLoading(state.stepQuizState)
            }
        }

        val feedbackState = stepQuizFeedbackMapper?.map(state)
        if (feedbackState != null) {
            stepQuizFeedbackBlocksDelegate?.setState(feedbackState)
        }

        when (val stepQuizState = state.stepQuizState) {
            is StepQuizFeature.StepQuizState.AttemptLoaded -> {
                renderAttemptLoaded(stepQuizState)
            }
            else -> {
                // no op
            }
        }

        parentOfType(StepToolbarHost::class.java)
            ?.renderPrimaryAction(
                StepMenuPrimaryAction.THEORY,
                StepMenuPrimaryActionParams(
                    isVisible = StepQuizResolver.isTheoryToolbarItemAvailable(state.stepQuizState),
                    isEnabled = !StepQuizResolver.isQuizLoading(state.stepQuizState)
                )
            )
        renderHints(state.stepQuizHintsState)

        onNewState(state)
    }

    private fun renderAttemptLoaded(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        descriptionBinding?.stepQuizDescription?.text =
            stepQuizTitleMapper?.getStepQuizTitle(
                blockName = step.block.name,
                isMultipleChoice = state.attempt.dataset?.isMultipleChoice,
                isCheckbox = state.attempt.dataset?.isCheckbox
            )
        stepQuizFormDelegate?.setState(state)
        viewBinding.stepQuizButtons.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)

        when (val submissionState = state.submissionState) {
            is StepQuizFeature.SubmissionState.Loaded -> {
                switchStepQuizButtonsState(getLoadedSubmissionButtonsState(submissionState.submission.status, step))
            }
            is StepQuizFeature.SubmissionState.Empty -> {
                switchStepQuizButtonsState(StepQuizButtonsState.Submit)
            }
        }
    }

    private fun getLoadedSubmissionButtonsState(submissionStatus: SubmissionStatus?, step: Step): StepQuizButtonsState =
        when (submissionStatus) {
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

    private fun switchStepQuizButtonsState(newState: StepQuizButtonsState) {
        stepQuizButtonsViewStateDelegate?.switchState(newState)
    }

    private fun renderHints(state: StepQuizHintsFeature.State) {
        val viewState = StepQuizHintsViewStateMapper.mapState(state)
        stepQuizHintsDelegate?.render(requireContext(), viewState)
    }

    private fun renderLimits(state: StepQuizToolbarFeature.State) {
        val viewState = StepQuizToolbarViewStateMapper.map(state)
        parentOfType(StepToolbarHost::class.java)
            ?.renderToolbarContent(StepToolbarContentViewState.Practice(viewState))
    }

    final override fun renderPracticeLoading(isPracticingLoading: Boolean) {
        if (isResumed) {
            with(viewBinding) {
                stepQuizButtons.stepQuizContinueButtonShimmer.isVisible = isPracticingLoading
                stepQuizButtons.stepQuizContinueButton.isEnabled = !isPracticingLoading
            }
        }
    }

    override fun problemOnboardingShown(modalType: StepQuizFeature.ProblemOnboardingModal) {
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.ProblemOnboardingModalShownMessage(modalType)
        )
    }

    override fun problemOnboardingHidden(modalType: StepQuizFeature.ProblemOnboardingModal) {
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.ProblemOnboardingModalHiddenMessage(modalType)
        )
    }

    override fun onLimitsClick() {
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.StepQuizToolbarMessage(StepQuizToolbarFeature.Message.ProblemsLimitClicked)
        )
    }

    override fun onTheoryClick() {
        stepQuizViewModel.onNewMessage(
            StepQuizFeature.Message.TheoryToolbarItemClicked
        )
    }

    protected fun syncReplyState(reply: Reply) {
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.SyncReply(reply))
    }

    protected fun onKeyboardStateChanged(isKeyboardShown: Boolean) {
        this.isKeyboardShown = isKeyboardShown
        with(viewBinding) {
            stepQuizButtons.root.isVisible = !isKeyboardShown
            stepQuizFeedbackBlocks.root.isVisible =
                !isKeyboardShown && stepQuizFeedbackBlocks.root.children.any { it.isVisible }
            updateStatisticsVisibility()
        }
    }

    private fun updateStatisticsVisibility() {
        with(viewBinding) {
            stepQuizStatistics.isVisible =
                !isKeyboardShown && stepQuizStatistics.text != null
        }
    }

    /**
     * Use only for analytic events logging.
     * @param message an analytic event message
     */
    protected fun logAnalyticEventMessage(message: StepQuizFeature.Message) {
        stepQuizViewModel.onNewMessage(message)
    }
}