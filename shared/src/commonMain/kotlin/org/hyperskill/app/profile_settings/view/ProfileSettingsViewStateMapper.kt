package org.hyperskill.app.profile_settings.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature.ViewState
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

internal class ProfileSettingsViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProfileSettingsFeature.State): ViewState =
        when (state) {
            ProfileSettingsFeature.State.Idle -> ViewState.Idle
            ProfileSettingsFeature.State.Loading -> ViewState.Loading
            is ProfileSettingsFeature.State.Content -> mapContentState(state)
        }

    private fun mapContentState(state: ProfileSettingsFeature.State.Content): ViewState.Content =
        ViewState.Content(
            profileSettings = state.profileSettings,
            isLoadingMagicLink = state.isLoadingMagicLink,
            subscriptionState = if (state.subscription != null && state.subscriptionFormattedPricePerMonth != null) {
                when {
                    state.subscription.type == SubscriptionType.MOBILE_ONLY -> {
                        ViewState.Content.SubscriptionState(
                            resourceProvider.getString(SharedResources.strings.settings_subscription_mobile_only)
                        )
                    }
                    state.subscription.type.canUpgradeToMobileOnly -> {
                        ViewState.Content.SubscriptionState(
                            resourceProvider.getString(
                                SharedResources.strings.settings_subscription_mobile_only_suggestion,
                                state.subscriptionFormattedPricePerMonth
                            )
                        )
                    }
                    else -> null
                }
            } else {
                null
            }
        )
}