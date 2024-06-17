package org.hyperskill.app.android.welcome_onbaording.finish.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.welcome_onbaording.finish.ui.WelcomeOnboardingFinish
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import ru.nobird.android.view.base.ui.extension.argument

class WelcomeOnboardingFinishFragment : Fragment() {
    companion object {
        fun newInstance(track: WelcomeOnboardingTrack): WelcomeOnboardingFinishFragment =
            WelcomeOnboardingFinishFragment().apply {
                this.track = track
            }
    }

    private var track: WelcomeOnboardingTrack by argument()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    OnComposableShownFirstTime(key = this@WelcomeOnboardingFinishFragment, block = ::onShow)
                    WelcomeOnboardingFinish()
                }
            }
        }

    private fun onShow() {
        (parentFragment as? WelcomeOnboardingHost)?.onFinishScreenShowed()
    }
}