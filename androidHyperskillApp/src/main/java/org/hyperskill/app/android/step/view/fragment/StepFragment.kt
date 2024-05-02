package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlin.math.roundToInt
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepBinding
import org.hyperskill.app.android.step.view.model.StepHost
import org.hyperskill.app.android.step.view.model.StepQuizToolbarCallback
import org.hyperskill.app.android.step.view.model.StepToolbarContentViewState
import org.hyperskill.app.android.step.view.model.StepToolbarHost
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class StepFragment : Fragment(R.layout.fragment_step), StepToolbarHost, StepHost {

    companion object {
        private const val STEP_WRAPPER_TAG = "step_wrapper"

        fun newInstance(stepRoute: StepRoute): Fragment =
            StepFragment()
                .apply {
                    this.stepRoute = stepRoute
                }
    }

    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    private val viewBinding: FragmentStepBinding by viewBinding(FragmentStepBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStepFragment()
        setupAppBar()
    }

    @Suppress("DEPRECATION")
    private fun setStepFragment() {
        setChildFragment(R.id.stepWrapperContainer, STEP_WRAPPER_TAG) {
            StepWrapperFragment.newInstance(stepRoute)
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(viewBinding.stepAppBar.stepToolbar)
        viewBinding.stepAppBar.stepToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.stepAppBar.stepQuizLimitsTextView.setOnClickListener {
            (childFragmentManager.findFragmentByTag(STEP_WRAPPER_TAG) as? StepQuizToolbarCallback)
                ?.onLimitsClicked()
        }
    }

    @Suppress("DEPRECATION", "MagicNumber")
    override fun reloadStep(stepRoute: StepRoute) {
        childFragmentManager
            .commitNow {
                setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out
                )
                replace(
                    R.id.stepWrapperContainer,
                    StepWrapperFragment.newInstance(stepRoute),
                    STEP_WRAPPER_TAG
                )
            }
    }

    override fun renderToolbarContent(viewState: StepToolbarContentViewState) {
        when (viewState) {
            is StepToolbarContentViewState.Practice -> {
                val stepQuizToolbarViewState = viewState.stepQuizToolbarViewState
                viewBinding.stepAppBar.stepTheoryToolbarTitle.isVisible = false
                viewBinding.stepAppBar.stepQuizLimitsTextView.isVisible =
                    stepQuizToolbarViewState is StepQuizToolbarFeature.ViewState.Content.Visible
                if (stepQuizToolbarViewState is StepQuizToolbarFeature.ViewState.Content.Visible) {
                    viewBinding.stepAppBar.stepQuizLimitsTextView.setTextIfChanged(
                        stepQuizToolbarViewState.stepsLimitLabel
                    )
                }
            }
            is StepToolbarContentViewState.Theory -> {
                viewBinding.stepAppBar.stepQuizLimitsTextView.isVisible = false
                with(viewBinding.stepAppBar.stepTheoryToolbarTitle) {
                    isVisible = true
                    setTextIfChanged(viewState.title)
                }
            }
        }
        TransitionManager.beginDelayedTransition(viewBinding.stepAppBar.stepToolbar)
    }

    @Suppress("MagicNumber")
    override fun renderTopicProgress(viewState: StepToolbarFeature.ViewState) {
        val isProgressVisible = viewState is StepToolbarFeature.ViewState.Content
        viewBinding.stepTopicProgressSeparator.isVisible = isProgressVisible
        viewBinding.stepTopicProgressIndicator.isVisible = isProgressVisible
        if (viewState is StepToolbarFeature.ViewState.Content) {
            setProgress(viewBinding.stepTopicProgressIndicator, viewState.progress)
        }
    }

    @Suppress("MagicNumber")
    private fun setProgress(
        progressIndicator: LinearProgressIndicator,
        progress: Float
    ) {
        val normalProgress = (progress * 100).roundToInt()
        if (normalProgress != progressIndicator.progress) {
            progressIndicator.setProgressCompat(
                /* progress = */ normalProgress,
                /* animated = */ normalProgress > progressIndicator.progress
            )
        }
    }
}