package org.hyperskill.app.welcome_onboarding.track_details.view

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack

internal val WelcomeOnboardingTrack.titleStringResource: StringResource
    get() = when (this) {
        WelcomeOnboardingTrack.JAVA -> SharedResources.strings.welcome_onboarding_track_details_java_title
        WelcomeOnboardingTrack.JAVA_SCRIPT -> SharedResources.strings.welcome_onboarding_track_details_js_title
        WelcomeOnboardingTrack.KOTLIN -> SharedResources.strings.welcome_onboarding_track_details_kotlin_title
        WelcomeOnboardingTrack.PYTHON -> SharedResources.strings.welcome_onboarding_track_details_python_title
        WelcomeOnboardingTrack.SQL -> SharedResources.strings.welcome_onboarding_track_details_sql_title
    }