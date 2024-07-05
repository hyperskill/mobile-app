package org.hyperskill.app.android.step.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Router
import com.google.android.material.progressindicator.LinearProgressIndicator
import dev.chrisbanes.insetter.applyInsetter
import kotlin.math.roundToInt
import kotlinx.serialization.builtins.ListSerializer
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepBinding
import org.hyperskill.app.android.step.view.delegate.StepMenuDelegate
import org.hyperskill.app.android.step.view.delegate.StepRouterDelegate
import org.hyperskill.app.android.step.view.model.OpenTheoryMenuAction
import org.hyperskill.app.android.step.view.model.StepHost
import org.hyperskill.app.android.step.view.model.StepQuizToolbarCallback
import org.hyperskill.app.android.step.view.model.StepToolbarCallback
import org.hyperskill.app.android.step.view.model.StepToolbarContentViewState
import org.hyperskill.app.android.step.view.model.StepToolbarHost
import org.hyperskill.app.android.step.view.navigation.StepNavigationContainer
import org.hyperskill.app.android.step.view.navigation.StepWrapperScreen
import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

@Suppress("DEPRECATION")
class StepFragment : Fragment(R.layout.fragment_step), StepToolbarHost, StepHost, StepNavigationContainer {

    companion object {
        fun newInstance(stepRoute: StepRoute): Fragment =
            StepFragment()
                .apply {
                    this.stepRoutesStack = listOf(stepRoute)
                }
    }

    // Stores stepRoutes opened in the navigation stack (practice step -> theory step)
    private var stepRoutesStack: List<StepRoute> by argument(serializer = ListSerializer(StepRoute.serializer()))

    private val currentStepRoute: StepRoute
        get() = stepRoutesStack.last()

    private val stepQuizToolbarCallback: StepQuizToolbarCallback?
        get() = childFragmentManager.findFragmentByTag(StepWrapperScreen.TAG) as? StepQuizToolbarCallback

    private val viewBinding: FragmentStepBinding by viewBinding(FragmentStepBinding::bind)

    private var stepMenuDelegate: StepMenuDelegate? = null

    private var stepRouterDelegate: StepRouterDelegate? = null

    override val router: Router
        get() = requireNotNull(stepRouterDelegate?.router)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stepRouterDelegate = StepRouterDelegate(
            containerId = R.id.stepWrapperContainer,
            fragment = this,
            onBackPressed = ::popStepRoute
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyWindowInsets()
        setupAppBar()
        setStepFragment()
    }

    private fun applyWindowInsets() {
        viewBinding.stepAppBar.stepToolbar.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }
    }

    private fun setStepFragment() {
        if (childFragmentManager.findFragmentByTag(StepWrapperScreen.TAG) == null) {
            router.newRootScreen(StepWrapperScreen(currentStepRoute))
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(viewBinding.stepAppBar.stepToolbar)
        stepMenuDelegate = StepMenuDelegate(
            viewLifecycleOwner = viewLifecycleOwner,
            menuHost = requireActivity() as MenuHost,
            onTheoryClick = ::onTheoryClick,
            onSecondaryActionClick = ::onSecondaryActionClick,
            onBackClick = ::onNavigationClick
        )
        viewBinding.stepAppBar.stepQuizLimitsTextView.setOnClickListener {
            stepQuizToolbarCallback?.onLimitsClick()
        }
    }

    private fun onNavigationClick() {
        if (childFragmentManager.fragments.isEmpty()) {
            requireRouter().exit()
        } else {
            popStepRoute()
            router.exit()
        }
    }

    override fun onDestroyView() {
        this.stepMenuDelegate = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        stepRouterDelegate = null
        super.onDestroy()
    }

    override fun reloadStep(stepRoute: StepRoute) {
        pushStepRoute(stepRoute)
        router.replaceScreen(StepWrapperScreen(stepRoute))
    }

    override fun navigateToTheory(stepRoute: StepRoute) {
        pushStepRoute(stepRoute)
        router.navigateTo(StepWrapperScreen(stepRoute))
    }

    override fun renderToolbarContent(viewState: StepToolbarContentViewState) {
        when (viewState) {
            is StepToolbarContentViewState.Practice -> {
                val stepQuizToolbarViewState = viewState.stepQuizToolbarViewState
                viewBinding.stepAppBar.stepTheoryToolbarTitle.isVisible = false
                if (!viewBinding.stepAppBar.stepQuizLimitsTextView.isVisible) {
                    viewBinding.stepAppBar.stepQuizLimitsTextView.isVisible =
                        stepQuizToolbarViewState is StepQuizToolbarFeature.ViewState.Content.Visible
                    TransitionManager.beginDelayedTransition(viewBinding.stepAppBar.stepToolbar)
                }
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
                renderAction(OpenTheoryMenuAction(isVisible = false, isEnabled = false))
                TransitionManager.beginDelayedTransition(viewBinding.stepAppBar.stepToolbar)
            }
        }
    }

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

    override fun renderAction(action: OpenTheoryMenuAction) {
        stepMenuDelegate?.renderMainMenuAction(action)
    }

    override fun renderSecondaryMenuActions(actions: Set<StepMenuAction>) {
        stepMenuDelegate?.renderSecondaryMenuActions(actions)
    }

    private fun onTheoryClick() {
        stepQuizToolbarCallback?.onTheoryClick()
    }

    private fun onSecondaryActionClick(action: StepMenuAction) {
        (childFragmentManager.findFragmentByTag(StepWrapperScreen.TAG) as? StepToolbarCallback)
            ?.onActionClicked(action)
    }

    @Suppress("MagicNumber")
    private fun popStepRoute() {
        stepRoutesStack = stepRoutesStack.dropLast(1)
    }

    private fun pushStepRoute(stepRoute: StepRoute) {
        stepRoutesStack = stepRoutesStack + stepRoute
    }
}