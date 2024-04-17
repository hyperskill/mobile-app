package org.hyperskill.app.problems_limit_reached.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.State
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isFreemiumWrongSubmissionChargeLimitsEnabled
import org.hyperskill.app.profile.domain.model.isMobileOnlySubscriptionEnabled
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.isFreemium

internal class ProblemsLimitReachedModalViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    fun map(state: State): ViewState {
        val isSubscriptionPurchaseEnabled = isSubscriptionPurchaseEnabled(state.profile, state.subscription)
        return if (state.profile.features.isFreemiumWrongSubmissionChargeLimitsEnabled) {
            getWrongSubmissionChargeViewState(state, isSubscriptionPurchaseEnabled)
        } else {
            getStepCountChargeViewState(state, isSubscriptionPurchaseEnabled)
        }
    }

    private fun isSubscriptionPurchaseEnabled(
        profile: Profile,
        subscription: Subscription
    ): Boolean =
        profile.features.isMobileOnlySubscriptionEnabled && subscription.isFreemium

    private fun getWrongSubmissionChargeViewState(
        state: State,
        isSubscriptionPurchaseEnabled: Boolean
    ): ViewState =
        ViewState(
            title = if (state.subscription.stepsLimitTotal != null) {
                resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_no_lives_left_title_template,
                    state.subscription.stepsLimitTotal
                )
            } else {
                resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_no_lives_left_title,
                )
            },
            description = resourceProvider.getString(
                if (isSubscriptionPurchaseEnabled) {
                    SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_lives_description
                } else {
                    SharedResources.strings.problems_limit_reached_modal_no_lives_left_description
                }
            ),
            unlockLimitsButtonText = if (isSubscriptionPurchaseEnabled) {
                resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_lives_button
                )
            } else {
                null
            }
        )

    private fun getStepCountChargeViewState(
        state: State,
        isSubscriptionPurchaseEnabled: Boolean
    ) =
        ViewState(
            title = resourceProvider.getString(SharedResources.strings.problems_limit_reached_modal_title),
            description = getStepCountChargeDescription(
                state.subscription.stepsLimitTotal,
                isSubscriptionPurchaseEnabled
            ),
            unlockLimitsButtonText = if (isSubscriptionPurchaseEnabled) {
                resourceProvider.getString(
                    SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_problems_button
                )
            } else {
                null
            }
        )

    private fun getStepCountChargeDescription(
        stepsLimitTotal: Int?,
        isSubscriptionPurchaseEnabled: Boolean
    ) =
        if (stepsLimitTotal != null) {
            resourceProvider.getString(
                if (isSubscriptionPurchaseEnabled) {
                    SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_problems_description_template
                } else {
                    SharedResources.strings.problems_limit_reached_modal_description_template
                },
                stepsLimitTotal
            )
        } else {
            resourceProvider.getString(
                if (isSubscriptionPurchaseEnabled) {
                    SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_problems_description
                } else {
                    SharedResources.strings.problems_limit_reached_modal_description
                }
            )
        }
}