package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.problems_limit.dialog.ProblemsLimitReachedBottomSheet
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.dialog.ProblemOnboardingBottomSheetDialogFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.android.step_quiz_hints.delegate.StepQuizHintsDelegate
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
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import org.hyperskill.app.step_quiz.view.mapper.StepQuizTitleMapper
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.mapper.StepQuizHintsViewStateMapper
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment :
    Fragment(R.layout.fragment_step_quiz),
    ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction>,
    StepCompletionView,
    MenuProvider,
    ProblemOnboardingBottomSheetDialogFragment.Callback {

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)

    private var stepQuizStateDelegate: ViewStateDelegate<StepQuizFeature.StepQuizState>? = null

    private var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate? = null
    private var stepQuizFormDelegate: StepQuizFormDelegate? = null
    private var stepQuizButtonsViewStateDelegate: ViewStateDelegate<StepQuizButtonsState>? = null

    private var stepQuizHintsDelegate: StepQuizHintsDelegate? = null

    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private var stepQuizTitleMapper: StepQuizTitleMapper? = null
    private val stepQuizFeedbackMapper by lazy(LazyThreadSafetyMode.NONE) {
        StepQuizFeedbackMapper()
    }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View
    protected abstract val descriptionBinding: LayoutStepQuizDescriptionBinding?

    protected var step: Step by argument(serializer = Step.serializer())
    protected var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    private var theoryButtonState: TheoryButtonState? = null

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
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        val stepView = createStepView(LayoutInflater.from(requireContext()), viewBinding.root)
        viewBinding.root.addView(stepView)

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
        stepQuizHintsDelegate = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.step_quiz_appbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        false

    override fun onPrepareMenu(menu: Menu) {
        val menuItem = menu.findItem(R.id.theory)
        menuItem.isVisible =
            theoryButtonState?.isVisible ?: false
        menuItem.actionView?.apply {
            isEnabled =
                theoryButtonState?.isEnabled ?: false
            setOnClickListener {
                stepQuizViewModel.onNewMessage(
                    StepQuizFeature.Message.TheoryToolbarItemClicked
                )
            }
        }
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
            is StepQuizFeature.Action.ViewAction.NavigateTo.Home -> {
                requireRouter().backTo(MainScreen(Tabs.TRAINING))
                mainScreenRouter.switch(Tabs.TRAINING)
            }
            is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen -> {
                requireRouter().navigateTo(StepScreen(action.stepRoute))
            }
            is StepQuizFeature.Action.ViewAction.RequestResetCode -> {
                requestResetCodeActionPermission()
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal -> {
                ProblemsLimitReachedBottomSheet.newInstance(action.modalData.title, action.modalData.description)
                    .showIfNotExists(childFragmentManager, ProblemsLimitReachedBottomSheet.TAG)
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemOnboardingModal -> {
                ProblemOnboardingBottomSheetDialogFragment.newInstance(action.modalType)
                    .showIfNotExists(
                        childFragmentManager,
                        ProblemOnboardingBottomSheetDialogFragment.TAG
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
        }
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

    override fun render(state: StepQuizFeature.State) {
        stepQuizStateDelegate?.switchState(state.stepQuizState)

        updateStatisticsVisibility()

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
                renderAttemptLoaded(stepQuizState)
            }
            else -> {
                // no op
            }
        }

        renderTheoryButton(state.stepQuizState)
        renderHints(state.stepQuizHintsState)

        onNewState(state)
    }

    private fun renderTheoryButton(state: StepQuizFeature.StepQuizState) {
        val newTheoryButtonState = TheoryButtonState(
            isVisible = StepQuizResolver.isTheoryToolbarItemAvailable(state),
            isEnabled = !StepQuizResolver.isQuizLoading(state)
        )
        if (newTheoryButtonState != this.theoryButtonState) {
            this.theoryButtonState = newTheoryButtonState
            (requireActivity() as MenuHost).invalidateMenu()
        }
    }

    private fun renderAttemptLoaded(state: StepQuizFeature.StepQuizState.AttemptLoaded) {
        descriptionBinding?.stepQuizDescription?.text =
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

    private fun renderHints(state: StepQuizHintsFeature.State) {
        val viewState = StepQuizHintsViewStateMapper.mapState(state)
        stepQuizHintsDelegate?.render(requireContext(), viewState)
    }

    final override fun render(isPracticingLoading: Boolean) {
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

    private sealed interface StepQuizButtonsState {
        object Submit : StepQuizButtonsState
        object Retry : StepQuizButtonsState
        object Continue : StepQuizButtonsState
        object RetryLogoAndSubmit : StepQuizButtonsState
        object RetryLogoAndContinue : StepQuizButtonsState
    }

    private data class TheoryButtonState(
        val isVisible: Boolean,
        val isEnabled: Boolean
    )
}