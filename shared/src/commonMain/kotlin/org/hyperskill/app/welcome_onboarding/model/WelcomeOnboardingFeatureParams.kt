package org.hyperskill.app.welcome_onboarding.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.profile.domain.model.Profile

@Serializable
data class WelcomeOnboardingFeatureParams(
    val profile: Profile,
    val isNotificationPermissionGranted: Boolean
)