package org.hyperskill.app.welcome_onboarding.root.view

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

class WelcomeOnboardingTrackViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun mapTrackToTrackViewState(track: WelcomeOnboardingTrack): WelcomeOnboardingTrackViewState =
        WelcomeOnboardingTrackViewState(
            track = track,
            title = resourceProvider.getString(SharedResources.strings.welcome_onboarding_track_details_title),
            trackTitle = resourceProvider.getString(getTrackTitleRes(track)),
            trackDescriptionHtml = resourceProvider.getString(getTrackDescriptionRes(track)),
            changeText = resourceProvider.getString(
                SharedResources.strings.welcome_onboarding_track_details_change_text
            ),
            buttonText = resourceProvider.getString(
                SharedResources.strings.welcome_onboarding_track_details_continue_btn
            )
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