package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepBinding
import org.hyperskill.app.android.step_practice.view.fragment.StepPracticeFragment
import org.hyperskill.app.android.step_theory.view.fragment.StepTheoryFragment
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StepFragment : Fragment(R.layout.fragment_step), ReduxView<StepFeature.State, StepFeature.Action.ViewAction>  {
    companion object {
        private const val STEP_TAG = "step"

        fun newInstance(stepId: Long): Fragment =
            StepFragment()
                .apply {
                    this.stepId = stepId
                }
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentStepBinding by viewBinding(FragmentStepBinding::bind)
    private val stepViewModel: StepViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<StepFeature.State> = ViewStateDelegate()

    private var stepId: Long by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        viewBinding.stepError.tryAgain.setOnClickListener {
            stepViewModel.onNewMessage(StepFeature.Message.Init(stepId, forceUpdate = true))
        }
        stepViewModel.onNewMessage(StepFeature.Message.Init(stepId))
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent()
        val platformStepComponent = HyperskillApp.graph().buildPlatformStepComponent(stepComponent)
        viewModelFactory = platformStepComponent.reduxViewModelFactory
    }

    private fun initViewStateDelegate() {
        viewStateDelegate.addState<StepFeature.State.Idle>()
        viewStateDelegate.addState<StepFeature.State.Loading>(viewBinding.stepProgress) // TODO Replace progress bar with skeletons
        viewStateDelegate.addState<StepFeature.State.Error>(viewBinding.stepError.root)
        viewStateDelegate.addState<StepFeature.State.Data>(viewBinding.stepContainer)
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: StepFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is StepFeature.State.Data) {
            initStepContainer(state.step)
        }
    }

    private fun initStepContainer(step: Step) {
        if (childFragmentManager.findFragmentByTag(STEP_TAG) != null) {
            return
        }

        val fragment = if (step.type == Step.Type.PRACTICE) {
            StepPracticeFragment.newInstance(step)
        } else {
            StepTheoryFragment.newInstance(step)
        }

        childFragmentManager
            .beginTransaction()
            .add(R.id.stepContainer, fragment, STEP_TAG)
            .commitNow()
    }
}