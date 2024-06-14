package org.hyperskill.app.android.welcome_onbaording.track.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.track.ui.WelcomeOnboardingTrackDetails
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.Action.ViewAction
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsViewModel
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.snackbar

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

    private var viewModelProvider: ViewModelProvider.Factory? = null
    private val welcomeOnboardingTrackDetailsViewModel: WelcomeOnboardingTrackDetailsViewModel by viewModels {
        requireNotNull(viewModelProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        welcomeOnboardingTrackDetailsViewModel.handleActions(this, ::onAction)
    }

    private fun injectComponent() {
        viewModelProvider =
            HyperskillApp
                .graph()
                .buildPlatformWelcomeOnboardingTrackDetailsComponent(track)
                .reduxViewModelFactory
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
                    WelcomeOnboardingTrackDetails(welcomeOnboardingTrackDetailsViewModel)
                }
            }
        }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NotifyTrackSelected -> {
                (parentFragment as? WelcomeOnboardingHost)?.onTrackSelected()
            }
            ViewAction.ShowTrackSelectionError -> {
                requireView().snackbar(org.hyperskill.app.R.string.common_error)
            }
        }
    }
}