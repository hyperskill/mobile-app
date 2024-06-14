package org.hyperskill.app.welcome_onboarding.track_details.view

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.State
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature.ViewState

internal class WelcomeOnboardingTrackDetailsViewStateMapper(
    private val resourceProvider: ResourceProvider
) {

    fun map(state: State): ViewState =
        ViewState(
            track = state.track,
            title = resourceProvider.getString(SharedResources.strings.welcome_onboarding_track_details_title),
            trackTitle = resourceProvider.getString(getTrackTitleRes(state.track)),
            trackDescriptionHtml = resourceProvider.getString(getTrackDescriptionRes(state.track)),
            changeText = resourceProvider.getString(
                SharedResources.strings.welcome_onboarding_track_details_change_text
            ),
            buttonText = resourceProvider.getString(
                SharedResources.strings.welcome_onboarding_track_details_continue_btn
            ),
            isLoadingShowed = state.isLoadingShowed
        )

    private fun getTrackTitleRes(track: WelcomeOnboardingTrack): StringResource =
        when (track) {
            WelcomeOnboardingTrack.JAVA -> SharedResources.strings.welcome_onboarding_track_details_java_title
            WelcomeOnboardingTrack.JAVA_SCRIPT -> SharedResources.strings.welcome_onboarding_track_details_js_title
            WelcomeOnboardingTrack.KOTLIN -> SharedResources.strings.welcome_onboarding_track_details_kotlin_title
            WelcomeOnboardingTrack.PYTHON -> SharedResources.strings.welcome_onboarding_track_details_python_title
            WelcomeOnboardingTrack.SQL -> SharedResources.strings.welcome_onboarding_track_details_sql_title
        }

    private fun getTrackDescriptionRes(track: WelcomeOnboardingTrack): StringResource =
        when (track) {
            WelcomeOnboardingTrack.JAVA ->
                SharedResources.strings.welcome_onboarding_track_details_java_description
            WelcomeOnboardingTrack.JAVA_SCRIPT ->
                SharedResources.strings.welcome_onboarding_track_details_js_description
            WelcomeOnboardingTrack.KOTLIN ->
                SharedResources.strings.welcome_onboarding_track_details_kotlin_description
            WelcomeOnboardingTrack.PYTHON ->
                SharedResources.strings.welcome_onboarding_track_details_python_description
            WelcomeOnboardingTrack.SQL ->
                SharedResources.strings.welcome_onboarding_track_details_sql_description
        }
}