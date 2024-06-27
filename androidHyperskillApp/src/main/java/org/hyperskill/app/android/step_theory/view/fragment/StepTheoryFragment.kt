package org.hyperskill.app.android.step_theory.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.databinding.FragmentStepTheoryBinding
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step.view.model.StepToolbarContentViewState
import org.hyperskill.app.android.step.view.model.StepToolbarHost
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.argument

class StepTheoryFragment :
    Fragment(R.layout.fragment_step_theory),
    StepCompletionView {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"

        fun newInstance(step: Step, stepRoute: StepRoute, isPracticingAvailable: Boolean): Fragment =
            StepTheoryFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
                this.isPracticingAvailable = isPracticingAvailable
            }
    }

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dateFormatter: SharedDateFormatter

    private val viewBinding: FragmentStepTheoryBinding by viewBinding(FragmentStepTheoryBinding::bind)

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())
    private var isPracticingAvailable: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
        dateFormatter = HyperskillApp.graph().commonComponent.dateFormatter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentOfType(StepToolbarHost::class.java)
            ?.renderToolbarContent(StepToolbarContentViewState.Theory(step.title))

        setupStartPracticeButton(isPracticingAvailable)

        renderSecondsToComplete(step.secondsToComplete)

        setupStepContentFragment(step)
    }

    private fun setupStartPracticeButton(
        isPracticingAvailable: Boolean
    ) {
        with(viewBinding.stepTheoryPracticeAction) {
            isVisible = isPracticingAvailable
            if (isPracticingAvailable) {
                doOnNextLayout {
                    updateContentBottomPadding()
                }
                setOnClickListener {
                    parentOfType(StepCompletionHost::class.java)
                        ?.onNewMessage(StepCompletionFeature.Message.StartPracticingClicked)
                }
            }
        }
    }

    private fun updateContentBottomPadding() {
        if (isResumed) {
            val buttonHeight = with(viewBinding.stepTheoryPracticeActionLayout) {
                height + marginBottom
            }
            val contentPadding = viewBinding.stepTheoryContentContainer.paddingBottom
            viewBinding.stepTheoryContentContainer.updatePadding(bottom = buttonHeight + contentPadding)
        }
    }

    private fun renderSecondsToComplete(secondsToComplete: Float?) {
        with(viewBinding.stepTheoryTimeToComplete) {
            isVisible = secondsToComplete != null
            if (secondsToComplete != null) {
                text = resourceProvider.getString(
                    SharedResources.strings.step_theory_reading_text,
                    dateFormatter.formatMinutesOrSecondsCount(secondsToComplete)
                )
            }
        }
    }

    private fun setupStepContentFragment(step: Step) {
        setChildFragment(R.id.stepTheoryContent, STEP_CONTENT_FRAGMENT_TAG) {
            TextStepContentFragment.newInstance(step)
        }
    }

    override fun renderPracticeLoading(isPracticingLoading: Boolean) {
        if (isResumed) {
            with(viewBinding) {
                stepTheoryPracticeActionShimmer.isVisible = isPracticingLoading
                stepTheoryPracticeAction.isEnabled = !isPracticingLoading
            }
        }
    }
}