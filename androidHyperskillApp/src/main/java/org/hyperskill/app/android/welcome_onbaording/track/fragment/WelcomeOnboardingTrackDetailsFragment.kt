package org.hyperskill.app.android.welcome_onbaording.track.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.track.ui.WelcomeOnboardingTrackDetails
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.view.WelcomeOnboardingTrackViewStateMapper
import ru.nobird.android.view.base.ui.extension.argument

class WelcomeOnboardingTrackDetailsFragment : Fragment() {
    companion object {
        fun newInstance(
            track: WelcomeOnboardingTrack
        ): WelcomeOnboardingTrackDetailsFragment =
            WelcomeOnboardingTrackDetailsFragment().apply {
                this.track = track
            }
    }

    private var track: WelcomeOnboardingTrack by argument()

    private var welcomeOnboardingTrackDetailsViewStateMapper: WelcomeOnboardingTrackViewStateMapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeOnboardingTrackDetailsViewStateMapper =
            HyperskillApp
                .graph()
                .buildWelcomeOnboardingTrackDetailsComponent()
                .welcomeOnboardingTrackDetailsViewStateMapper
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
                    val viewState = remember {
                        requireNotNull(welcomeOnboardingTrackDetailsViewStateMapper)
                            .mapTrackToTrackViewState(track)
                    }
                    WelcomeOnboardingTrackDetails(viewState = viewState, onSelectClick = ::onSelectClick)
                }
            }
        }

    private fun onSelectClick() {
        (parentFragment as? WelcomeOnboardingHost)?.onTrackSelected(track)
    }
}