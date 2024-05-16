package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.databinding.FragmentStepWrapperBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.share_streak.fragment.ShareStreakDialogFragment
import org.hyperskill.app.android.step.view.delegate.StepDelegate
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.model.StepQuizToolbarCallback
import org.hyperskill.app.android.step.view.model.StepToolbarHost
import org.hyperskill.app.android.step_practice.view.fragment.StepPracticeFragment
import org.hyperskill.app.android.step_theory.view.fragment.StepTheoryFragment
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

/**
 * A wrapper fragment that runs [StepFeature]
 * and hosts [StepPracticeFragment] or [StepTheoryFragment].
 */
@Deprecated("Should not be used directly, only via StepWrapperScreen")
class StepWrapperFragment :
    Fragment(R.layout.fragment_step_wrapper),
    ReduxView<StepFeature.ViewState, StepFeature.Action.ViewAction>,
    StepCompletionHost,
    ShareStreakDialogFragment.Callback,
    StepQuizToolbarCallback {

    companion object {
        private const val STEP_CONTENT_TAG = "step_content"

        @Suppress("DEPRECATION")
        fun newInstance(stepRoute: StepRoute): Fragment =
            StepWrapperFragment()
                .apply {
                    this.stepRoute = stepRoute
                }
    }

    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val stepViewModel: StepViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private val viewBinding: FragmentStepWrapperBinding by viewBinding(FragmentStepWrapperBinding::bind)

    private var viewStateDelegate: ViewStateDelegate<StepFeature.StepState>? = null

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewStateDelegate()
        StepDelegate.init(
            errorBinding = viewBinding.stepError,
            lifecycle = viewLifecycleOwner.lifecycle,
            onNewMessage = stepViewModel::onNewMessage
        )
    }

    private fun injectComponent() {
        viewModelFactory = HyperskillApp.graph().buildPlatformStepComponent(stepRoute).reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<StepFeature.StepState>().apply {
            addState<StepFeature.StepState.Idle>()
            addState<StepFeature.StepState.Loading>(viewBinding.stepProgress)
            addState<StepFeature.StepState.Error>(viewBinding.stepError.root)
            addState<StepFeature.StepState.Data>(viewBinding.stepContainer)
        }
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        StepDelegate.onAction(
            fragment = this,
            mainScreenRouter = mainScreenRouter,
            action = action
        )
    }

    override fun render(state: StepFeature.ViewState) {
        val stepState = state.stepState
        viewStateDelegate?.switchState(stepState)

        if (stepState is StepFeature.StepState.Data) {
            initStepContainer(stepState)
            parentOfType(StepToolbarHost::class.java)?.renderTopicProgress(state.stepToolbarViewState)
            (childFragmentManager.findFragmentByTag(STEP_CONTENT_TAG) as? StepCompletionView)
                ?.renderPracticeLoading(stepState.stepCompletionState.isPracticingLoading)
        }
    }

    private fun initStepContainer(data: StepFeature.StepState.Data) {
        setChildFragment(R.id.stepContainer, STEP_CONTENT_TAG) {
            when (data.step.type) {
                Step.Type.PRACTICE -> StepPracticeFragment.newInstance(data.step, stepRoute)
                Step.Type.THEORY -> StepTheoryFragment.newInstance(data.step, stepRoute, data.isPracticingAvailable)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onNewMessage(message: StepCompletionFeature.Message) {
        stepViewModel.onNewMessage(
            StepFeature.Message.StepCompletionMessage(message)
        )
    }

    override fun onShareStreakBottomSheetShown(streak: Int) {
        stepViewModel.onShareStreakBottomSheetShown(streak)
    }

    override fun onShareStreakBottomSheetDismissed(streak: Int) {
        stepViewModel.onShareStreakBottomSheetDismissed(streak)
    }

    override fun onRefuseStreakSharingClick(streak: Int) {
        stepViewModel.onRefuseStreakSharingClick(streak)
    }

    override fun onShareClick(streak: Int) {
        stepViewModel.onShareClick(streak)
    }

    override fun onLimitsClick() {
        (childFragmentManager.findFragmentByTag(STEP_CONTENT_TAG) as? StepQuizToolbarCallback)
            ?.onLimitsClick()
    }

    override fun onTheoryClick() {
        (childFragmentManager.findFragmentByTag(STEP_CONTENT_TAG) as? StepQuizToolbarCallback)
            ?.onTheoryClick()
    }
}