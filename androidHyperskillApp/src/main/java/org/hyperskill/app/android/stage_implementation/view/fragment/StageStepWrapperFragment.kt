package org.hyperskill.app.android.stage_implementation.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStageStepWrapperBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.step.view.delegate.StepDelegate
import org.hyperskill.app.android.step.view.fragment.StepFragment
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

/**
 * A wrapper fragment around StepQuiz.
 * Analog of [StepFragment] with special ui for StageImplementation.
 * Able to launch only stepQuiz, not a stepTheory.
 *
 * Should not be used directly, only via [StageImplementationFragment]
 */
@Deprecated("Should not be used directly, only via StageImplementationFragment")
class StageStepWrapperFragment :
    Fragment(R.layout.fragment_stage_step_wrapper),
    ReduxView<StepFeature.State, StepFeature.Action.ViewAction>,
    StepCompletionHost {

    companion object {
        private const val STEP_DESCRIPTION_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"

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

    private var viewStateDelegate: ViewStateDelegate<StepFeature.State>? = null

    private val stepDelegate: StepDelegate by lazy(LazyThreadSafetyMode.NONE) {
        StepDelegate(
            fragment = this,
            rootView = viewBinding.root,
            onRequestDailyStudyRemindersPermissionResult = { isGranted ->
                onNewMessage(StepCompletionFeature.Message.RequestDailyStudyRemindersPermissionResult(isGranted))
            }
        )
    }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent(stepRoute)
        val platformStepComponent = HyperskillApp.graph().buildPlatformStepComponent(stepComponent)
        viewModelFactory = platformStepComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = ViewStateDelegate<StepFeature.State>().apply {
            addState<StepFeature.State.Idle>()
            addState<StepFeature.State.Loading>(viewBinding.stageImplementationProgress)
            addState<StepFeature.State.Error>(viewBinding.stageImplementationError.root)
            addState<StepFeature.State.Data>(
                viewBinding.stageImplementationAppBar.root,
                viewBinding.stagePracticeContainer
            )
        }
        with(viewBinding.stageImplementationAppBar) {
            stageImplementationToolbarTitle.text = navigationTitle
            stageImplementationToolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
        }
        viewBinding.stageImplementationTitle.text = stageTitle
        stepDelegate.init(viewBinding.stageImplementationError, stepViewModel::onNewMessage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun render(state: StepFeature.State) {
        viewStateDelegate?.switchState(state)
        if (state is StepFeature.State.Data) {
            (childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) as? StepCompletionView)
                ?.render(state.stepCompletionState.isPracticingLoading)
            initStepTheoryFragment(state.step)
            initStepQuizFragment(state.step, stepRoute)
        }
    }

    private fun initStepTheoryFragment(step: Step) {
        setChildFragment(R.id.stageDescriptionContainer, STEP_DESCRIPTION_FRAGMENT_TAG) {
            TextStepContentFragment.newInstance(step)
        }
    }

    private fun initStepQuizFragment(step: Step, stepRoute: StepRoute) {
        setChildFragment(R.id.stageQuizContainer, STEP_QUIZ_FRAGMENT_TAG) {
            StepQuizFragmentFactory.getQuizFragment(step, stepRoute)
        }
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        stepDelegate.onAction(
            mainScreenRouter = mainScreenRouter,
            action = action
        )
    }

    override fun onNewMessage(message: StepCompletionFeature.Message) {
        stepViewModel.onNewMessage(StepFeature.Message.StepCompletionMessage(message))
    }
}