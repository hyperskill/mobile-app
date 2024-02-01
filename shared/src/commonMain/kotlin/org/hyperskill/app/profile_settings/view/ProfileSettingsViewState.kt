package org.hyperskill.app.profile_settings.view

import org.hyperskill.app.profile_settings.domain.model.ProfileSettings

data class ProfileSettingsViewState(
    val profileSettings: ProfileSettings,
    val isLoadingMagicLink: Boolean,
    val subscriptionState: SubscriptionState?
) {
    data class SubscriptionState(
        val description: String
    )
}