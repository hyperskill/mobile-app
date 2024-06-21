package org.hyperskill.app.android.welcome_onbaording.track.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.hyperskill.app.R
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature

object WelcomeOnboardingTrackDetailsPreviewData {
    val PreviewViewState: WelcomeOnboardingTrackDetailsFeature.ViewState
        @Composable
        get() = WelcomeOnboardingTrackDetailsFeature.ViewState(
            track = WelcomeOnboardingTrack.KOTLIN,
            title = stringResource(id = R.string.welcome_onboarding_track_details_title),
            trackTitle = stringResource(id = R.string.welcome_onboarding_track_details_kotlin_title),
            trackDescriptionHtml = stringResource(
                id = R.string.welcome_onboarding_track_details_kotlin_description
            ),
            changeText = stringResource(id = R.string.welcome_onboarding_track_details_change_text),
            buttonText = stringResource(id = R.string.welcome_onboarding_track_details_continue_btn),
            isLoadingShowed = false
        )
}