package org.hyperskill.app.android.debug.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.debug.ui.DebugScreen
import org.hyperskill.app.android.stage_implementation.view.navigation.StageImplementationScreen
import org.hyperskill.app.android.step.view.navigation.StepScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugViewModel
import ru.nobird.android.view.base.ui.extension.argument

class DebugFragment : Fragment(R.layout.fragment_debug) {

    companion object {
        @Deprecated(
            "Should not be accessed directly, only through DebugScreen",
            replaceWith = ReplaceWith(
                "DebugScreen",
                imports = arrayOf(
                    "org.hyperskill.app.android.debug.DebugScreen"
                )
            )
        )
        fun newInstance(isBackNavigationEnabled: Boolean): Fragment =
            DebugFragment().apply {
                this.isBackNavigationEnabled = isBackNavigationEnabled
            }
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val debugViewModel: DebugViewModel by viewModels { viewModelFactory }

    private var isBackNavigationEnabled: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val debugComponent = HyperskillApp.graph().buildDebugComponent()
        val platformComponent = HyperskillApp.graph().buildPlatformDebugComponent(debugComponent)
        viewModelFactory = platformComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (view as ComposeView).setContent {
            HyperskillTheme {
                DebugScreen(
                    viewModel = debugViewModel,
                    onBackClick = ::onBackClick.takeIf { isBackNavigationEnabled }
                )
            }
        }
        debugViewModel.handleActions(viewLifecycleOwner, onAction = ::handleAction)
    }

    private fun handleAction(action: DebugFeature.Action.ViewAction) {
        when (action) {
            is DebugFeature.Action.ViewAction.OpenStep -> {
                requireRouter().navigateTo(StepScreen(action.stepRoute))
            }
            DebugFeature.Action.ViewAction.RestartApplication -> {
                Toast.makeText(
                    requireContext(),
                    R.string.debug_restarting_message,
                    Toast.LENGTH_SHORT
                ).show()
                view?.postDelayed({ triggerApplicationRestart(requireContext()) }, 1500)
            }
            is DebugFeature.Action.ViewAction.OpenStageImplement -> {
                requireRouter().navigateTo(
                    StageImplementationScreen(
                        projectId = action.projectId,
                        stageId = action.stageId
                    )
                )
            }
        }
    }

    private fun onBackClick() {
        requireRouter().exit()
    }

    @Suppress("MagicNumber")
    private fun triggerApplicationRestart(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}