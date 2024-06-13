package org.hyperskill.app.android.welcome_onbaording.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.ui.WelcomeOnboardingEntryPoint

class WelcomeOnboardingStartingFragment : Fragment() {
    companion object {
        fun newInstance(): WelcomeOnboardingStartingFragment =
            WelcomeOnboardingStartingFragment()
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