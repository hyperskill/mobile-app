package org.hyperskill.app.profile_settings.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile_settings.presentation.ProfileSettingsFeature
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

class ProfileSettingsViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ProfileSettingsFeature.State.Content): ProfileSettingsViewState =
        ProfileSettingsViewState(
            profileSettings = state.profileSettings,
            isLoadingMagicLink = state.isLoadingMagicLink,
            subscriptionState = if (isSubscriptionVisible(state)) {
                when (state.subscription?.type) {
                    SubscriptionType.MOBILE_ONLY ->
                        ProfileSettingsViewState.SubscriptionState(
                            resourceProvider.getString(SharedResources.strings.settings_subscription_mobile_only)
                        )
                    SubscriptionType.FREEMIUM -> {
                        state.mobileOnlyFormattedPrice?.let {
                            ProfileSettingsViewState.SubscriptionState(
                                resourceProvider.getString(
                                    SharedResources.strings.settings_subscription_mobile_only_suggestion,
                                    state.mobileOnlyFormattedPrice
                                )
                            )
                        }
                    }
                    else -> null
                }
            } else {
                null
            }
        )

    private fun isSubscriptionVisible(state: ProfileSettingsFeature.State.Content): Boolean =
        state.subscription != null &&
            state.mobileOnlyFormattedPrice != null &&
            when (state.subscription.type) {
                SubscriptionType.FREEMIUM,
                SubscriptionType.MOBILE_ONLY -> true
                else -> false
            }
}