package org.hyperskill.app.android.debug.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.debug.ui.DebugScreen
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.debug.presentation.DebugViewModel

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
        fun newInstance(): Fragment =
            DebugFragment()
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val debugViewModel: DebugViewModel by viewModels { viewModelFactory }

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
            MdcTheme {
                DebugScreen(debugViewModel)
            }
        }
        debugViewModel.handleActions(viewLifecycleOwner, block = ::handleAction)
    }

    private fun handleAction(action: DebugFeature.Action.ViewAction) {
        when (action) {
            is DebugFeature.Action.ViewAction.OpenStep -> {
                requireRouter().navigateTo(StepScreen(action.stepRoute))
            }
            DebugFeature.Action.ViewAction.RestartApplication -> {
                val context = requireContext()
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                val componentName = intent?.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                context.startActivity(mainIntent)
                Runtime.getRuntime().exit(0)
            }
        }
    }
}