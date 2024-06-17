package org.hyperskill.app.android.welcome_onbaording.entry_point.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.entry_point.ui.WelcomeOnboardingEntryPoint
import org.hyperskill.app.android.welcome_onbaording.root.model.WelcomeOnboardingHost

class WelcomeOnboardingEntryPointFragment : Fragment() {
    companion object {
        fun newInstance(): WelcomeOnboardingEntryPointFragment =
            WelcomeOnboardingEntryPointFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    WelcomeOnboardingEntryPoint(onStartClick = ::onStartClick)
                }
            }
        }

    private fun onStartClick() {
        (parentFragment as? WelcomeOnboardingHost)?.onStartClick()
    }
}