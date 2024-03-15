package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.databinding.FragmentStepBinding
import org.hyperskill.app.android.interview_preparation.dialog.InterviewPreparationFinishedDialogFragment
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.share_streak.fragment.ShareStreakDialogFragment
import org.hyperskill.app.android.step.view.delegate.StepDelegate
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
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

class StepFragment :
    Fragment(R.layout.fragment_step),
    ReduxView<StepFeature.State, StepFeature.Action.ViewAction>,
    StepCompletionHost,
    ShareStreakDialogFragment.Callback,
    InterviewPreparationFinishedDialogFragment.Callback {

    companion object {
        private const val STEP_TAG = "step"

        fun newInstance(stepRoute: StepRoute): Fragment =
            StepFragment()
                .apply {
                    this.stepRoute = stepRoute
                }
    }

    private var stepDelegate: StepDelegate<StepFragment>? = null

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentStepBinding by viewBinding(FragmentStepBinding::bind)
    private val stepViewModel: StepViewModel by reduxViewModel(this) { viewModelFactory }
    private var viewStateDelegate: ViewStateDelegate<StepFeature.State>? = null
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        stepDelegate = StepDelegate(fragment = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewStateDelegate()
        stepDelegate?.init(
            errorBinding = viewBinding.stepError,
            lifecycle = viewLifecycleOwner.lifecycle,
            onNewMessage = stepViewModel::onNewMessage
        )
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent(stepRoute)
        val platformStepComponent = HyperskillApp.graph().buildPlatformStepComponent(stepComponent)
        viewModelFactory = platformStepComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<StepFeature.State>().apply {
            addState<StepFeature.State.Idle>()
            addState<StepFeature.State.Loading>(viewBinding.stepProgress)
            addState<StepFeature.State.Error>(viewBinding.stepError.root)
            addState<StepFeature.State.Data>(viewBinding.stepContainer)
        }
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        stepDelegate?.onAction(
            mainScreenRouter = mainScreenRouter,
            action = action
        )
    }

    override fun render(state: StepFeature.State) {
        viewStateDelegate?.switchState(state)
        if (state is StepFeature.State.Data) {
            initStepContainer(state)
            (childFragmentManager.findFragmentByTag(STEP_TAG) as? StepCompletionView)
                ?.render(state.stepCompletionState.isPracticingLoading)
        }
    }

    private fun initStepContainer(data: StepFeature.State.Data) {
        setChildFragment(R.id.stepContainer, STEP_TAG) {
            if (data.step.type == Step.Type.PRACTICE) {
                StepPracticeFragment.newInstance(data.step, stepRoute)
            } else {
                StepTheoryFragment.newInstance(data.step, stepRoute, data.isPracticingAvailable)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stepDelegate = null
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

    override fun onInterviewPreparationFinishedDialogShown() {
        stepViewModel.onInterviewPreparationFinishedDialogShown()
    }

    override fun onInterviewPreparationFinishedDialogHidden() {
        stepViewModel.onInterviewPreparationFinishedDialogHidden()
    }

    override fun onInterviewPreparationFinishedDialogGoTrainingClicked() {
        stepViewModel.onInterviewPreparationFinishedDialogGoTrainingClicked()
    }
}