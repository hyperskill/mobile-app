package org.hyperskill.app.android.stage_implementation.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.chrisbanes.insetter.applyInsetter
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.logger
import org.hyperskill.app.android.core.extensions.smoothScrollTo
import org.hyperskill.app.android.core.extensions.smoothScrollToBottom
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStageStepWrapperBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.share_streak.fragment.ShareStreakDialogFragment
import org.hyperskill.app.android.stage_implementation.delegate.StageStepMenuDelegate
import org.hyperskill.app.android.step.view.delegate.StepDelegate
import org.hyperskill.app.android.step.view.fragment.StepWrapperFragment
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step_practice.model.StepPracticeHost
import org.hyperskill.app.android.step_practice.view.fragment.StepPracticeDetailsFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.android.topic_completion.fragment.TopicCompletedDialogFragment
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

/**
 * A wrapper fragment around StepQuiz.
 * Analog of [StepWrapperFragment] with special ui for StageImplementation.
 * Able to launch only stepQuiz, not a stepTheory.
 *
 * Should not be used directly, only via [StageImplementationFragment]
 */
@Deprecated("Should not be used directly, only via StageImplementationFragment")
class StageStepWrapperFragment :
    Fragment(R.layout.fragment_stage_step_wrapper),
    ReduxView<StepFeature.ViewState, StepFeature.Action.ViewAction>,
    StepCompletionHost,
    ShareStreakDialogFragment.Callback,
    TopicCompletedDialogFragment.Callback,
    StepPracticeHost {

    companion object {
        private const val STEP_DESCRIPTION_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"
        private const val LOGGER_TAG = "StageStepWrapperFragment"

        private const val SMOOTH_SCROLL_DURATION_MILLISECONDS = 500

        @Suppress("DEPRECATION")
        fun newInstance(
            stepRoute: StepRoute,
            navigationTitle: String,
            stageTitle: String
        ): StageStepWrapperFragment =
            StageStepWrapperFragment()
                .apply {
                    this.stepRoute = stepRoute
                    this.navigationTitle = navigationTitle
                    this.stageTitle = stageTitle
                }
    }

    private var stepRoute: StepRoute by argument(StepRoute.serializer())
    private var navigationTitle: String by argument()
    private var stageTitle: String by argument()

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val stepViewModel: StepViewModel by reduxViewModel(this) { viewModelFactory }

    private val viewBinding: FragmentStageStepWrapperBinding by viewBinding(FragmentStageStepWrapperBinding::bind)

    private var viewStateDelegate: ViewStateDelegate<StepFeature.StepState>? = null

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    private val logger by logger(LOGGER_TAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory = HyperskillApp.graph().buildPlatformStepComponent(stepRoute).reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applyWindowInsets()
        viewStateDelegate = ViewStateDelegate<StepFeature.StepState>().apply {
            addState<StepFeature.StepState.Idle>()
            addState<StepFeature.StepState.Loading>(viewBinding.stageImplementationProgress)
            addState<StepFeature.StepState.Error>(viewBinding.stageImplementationError.root)
            addState<StepFeature.StepState.Data>(
                viewBinding.stageImplementationAppBar.root,
                viewBinding.stagePracticeContainer
            )
        }
        viewBinding.stageImplementationAppBar.stageImplementationToolbarTitle.text = navigationTitle
        viewBinding.stageImplementationTitle.text = stageTitle
        StepDelegate.init(
            errorBinding = viewBinding.stageImplementationError,
            lifecycle = viewLifecycleOwner.lifecycle,
            onNewMessage = stepViewModel::onNewMessage
        )
        setupMenu()
    }

    private fun applyWindowInsets() {
        viewBinding.stageImplementationAppBar.stageImplementationToolbar.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }
        viewBinding.stageImplementationError.root.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }
        viewBinding.stagePracticeContainer.applyInsetter {
            type(ime = true) {
                margin()
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(viewBinding.stageImplementationAppBar.stageImplementationToolbar)
        StageStepMenuDelegate.setup(
            menuHost = requireActivity() as MenuHost,
            viewLifecycleOwner = viewLifecycleOwner,
            onActionClick = stepViewModel::onActionClick,
            onBackClick = { requireRouter().exit() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun render(state: StepFeature.ViewState) {
        val stepState = state.stepState
        viewStateDelegate?.switchState(stepState)

        if (stepState is StepFeature.StepState.Data) {
            (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepCompletionView)
                ?.renderPracticeLoading(stepState.stepCompletionState.isPracticingLoading)
            initStepTheoryFragment(stepState.step, stepRoute)
            initStepQuizFragment(stepState.step, stepRoute)
        }
    }

    private fun initStepTheoryFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stageDescriptionContainer, STEP_DESCRIPTION_FRAGMENT_TAG) {
            StepPracticeDetailsFragment.newInstance(step, stepRoute)
        }
    }

    private fun initStepQuizFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stageQuizContainer, STEP_QUIZ_FRAGMENT_TAG) {
            StepQuizFragmentFactory.getQuizFragment(step, stepRoute)
        }
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        StepDelegate.onAction(
            fragment = this,
            mainScreenRouter = mainScreenRouter,
            action = action,
            logger = logger
        )
    }

    override fun onNewMessage(message: StepCompletionFeature.Message) {
        stepViewModel.onNewMessage(StepFeature.Message.StepCompletionMessage(message))
    }

    override fun onShareStreakBottomSheetShown(streak: Int) {
        stepViewModel.onShareStreakBottomSheetShown(streak)
    }

    override fun onShareStreakBottomSheetDismissed(streak: Int) {
        stepViewModel.onShareStreakBottomSheetDismissed(streak)
    }

    override fun onShareClick(streak: Int) {
        stepViewModel.onShareStreakClick(streak)
    }

    override fun onRefuseStreakSharingClick(streak: Int) {
        stepViewModel.onRefuseStreakSharingClick(streak)
    }

    override fun navigateTo(destination: TopicCompletedModalFeature.Action.ViewAction.NavigateTo) {
        when (destination) {
            TopicCompletedModalFeature.Action.ViewAction.NavigateTo.NextTopic ->
                onNewMessage(StepCompletionFeature.Message.TopicCompletedModalContinueNextTopicClicked)
            TopicCompletedModalFeature.Action.ViewAction.NavigateTo.StudyPlan ->
                onNewMessage(StepCompletionFeature.Message.TopicCompletedModalGoToStudyPlanClicked)
            is TopicCompletedModalFeature.Action.ViewAction.NavigateTo.Paywall ->
                onNewMessage(
                    StepCompletionFeature.Message.TopicCompletedModalPaywallClicked(
                        destination.paywallTransitionSource
                    )
                )
        }
    }

    override fun fullScrollDown() {
        viewBinding
            .stagePracticeContainer
            .smoothScrollToBottom(SMOOTH_SCROLL_DURATION_MILLISECONDS)
    }

    override fun scrollTo(view: View) {
        viewBinding
            .stagePracticeContainer
            .smoothScrollTo(view, SMOOTH_SCROLL_DURATION_MILLISECONDS)
    }
}