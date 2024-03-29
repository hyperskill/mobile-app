package org.hyperskill.app.manage_subscription.view.mapper

import kotlinx.datetime.Instant
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.ViewState
import org.hyperskill.app.manage_subscription.presentation.isSubscriptionManagementEnabled
import org.hyperskill.app.subscriptions.domain.model.isExpired

internal class ManageSubscriptionViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: SharedDateFormatter
) {
    fun map(state: State): ViewState =
        when (state) {
            State.Idle -> ViewState.Idle
            State.Loading -> ViewState.Loading
            State.Error -> ViewState.Error
            is State.Content -> mapContent(state)
        }

    private fun mapContent(content: State.Content): ViewState.Content =
        ViewState.Content(
            validUntilFormatted = content.subscription.validTill?.let(::formatValidUntil),
            buttonText = when {
                content.isSubscriptionManagementEnabled ->
                    resourceProvider.getString(SharedResources.strings.manage_subscription_manage_btn)
                content.subscription.isExpired ->
                    resourceProvider.getString(SharedResources.strings.manage_subscription_renew_btn)
                else -> null
            }
        )

    private fun formatValidUntil(validUntil: Instant): String {
        val formattedDate: String = dateFormatter.formatSubscriptionValidUntil(validUntil)
        return resourceProvider.getString(
            SharedResources.strings.manage_subscription_valid_until,
            formattedDate
        )
    }
}