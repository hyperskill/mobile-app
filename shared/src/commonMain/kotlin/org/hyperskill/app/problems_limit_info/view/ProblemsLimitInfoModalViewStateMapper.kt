package org.hyperskill.app.problems_limit_info.view

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.State
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy

internal class ProblemsLimitInfoModalViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val sharedDateFormatter: SharedDateFormatter
) {
    fun map(state: State): ViewState =
        when (state.launchSource) {
            ProblemsLimitInfoModalLaunchSource.USER_INITIATED -> getUserInitiatedViewState(state)
            ProblemsLimitInfoModalLaunchSource.AUTOMATIC_NO_LIMITS_LEFT -> getAutomaticNoLimitsLeftViewState(state)
        }

    @Suppress("MagicNumber")
    private fun getAutomaticNoLimitsLeftViewState(state: State): ViewState =
        ViewState(
            title = getNoLimitsLeftTitle(state.chargeLimitsStrategy),
            limitsDescription = null,
            animation = ViewState.Animation.NO_LIMITS_LEFT,
            leftLimitsText = getLeftLimitsText(state.chargeLimitsStrategy, 0),
            resetInText = getResetInText(state.subscription.stepsLimitResetTime),
            unlockDescription = getUnlockDescription(state.chargeLimitsStrategy),
            buttonText = getButtonText()
        )

    private fun getNoLimitsLeftTitle(chargeLimitsStrategy: FreemiumChargeLimitsStrategy): String =
        resourceProvider.getString(
            when (chargeLimitsStrategy) {
                FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_no_lives_left_title
                FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_no_limits_left_title
            }
        )

    private fun getUnlockDescription(chargeLimitsStrategy: FreemiumChargeLimitsStrategy): String =
        resourceProvider.getString(
            when (chargeLimitsStrategy) {
                FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_unlock_unlimited_lives_description
                FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_unlock_unlimited_problems_description
            }
        )

    private fun getUserInitiatedViewState(state: State): ViewState =
        ViewState(
            title = getUserInitiatedTitle(
                leftLimit = state.subscription.stepsLimitLeft,
                limitTotal = state.subscription.stepsLimitTotal,
                chargeLimitsStrategy = state.chargeLimitsStrategy
            ),
            limitsDescription = getUserInitiatedLimitsDescription(state.chargeLimitsStrategy),
            animation = getUserInitiatedAnimation(
                leftLimit = state.subscription.stepsLimitLeft,
                limitTotal = state.subscription.stepsLimitTotal
            ),
            leftLimitsText = getLeftLimitsText(
                chargeLimitsStrategy = state.chargeLimitsStrategy,
                leftLimit = state.subscription.stepsLimitLeft
            ),
            resetInText = getResetInText(state.subscription.stepsLimitResetTime),
            unlockDescription = null,
            buttonText = getButtonText()
        )

    @Suppress("MagicNumber")
    private fun getUserInitiatedTitle(
        leftLimit: Int?,
        limitTotal: Int?,
        chargeLimitsStrategy: FreemiumChargeLimitsStrategy
    ): String =
        when (leftLimit) {
            null, 0 -> getNoLimitsLeftTitle(chargeLimitsStrategy)
            limitTotal ->
                resourceProvider.getString(SharedResources.strings.problems_limit_info_modal_full_limits_title)
            else ->
                resourceProvider.getString(
                    when (chargeLimitsStrategy) {
                        FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION ->
                            SharedResources.strings.problems_limit_info_modal_part_spent_lives_title
                        FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION ->
                            SharedResources.strings.problems_limit_info_modal_part_spent_limits_title
                    }
                )
        }

    private fun getUserInitiatedLimitsDescription(chargeLimitsStrategy: FreemiumChargeLimitsStrategy): String =
        resourceProvider.getString(
            when (chargeLimitsStrategy) {
                FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_lives_description
                FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION ->
                    SharedResources.strings.problems_limit_info_modal_problems_limits_description
            }
        )

    @Suppress("MagicNumber")
    private fun getUserInitiatedAnimation(leftLimit: Int?, limitTotal: Int?): ViewState.Animation =
        when (leftLimit) {
            null, 0 -> ViewState.Animation.NO_LIMITS_LEFT
            limitTotal -> ViewState.Animation.FULL_LIMITS
            else -> ViewState.Animation.PARTIALLY_SPENT_LIMITS
        }

    private fun getLeftLimitsText(
        chargeLimitsStrategy: FreemiumChargeLimitsStrategy,
        leftLimit: Int?
    ): String? =
        if (leftLimit != null) {
            resourceProvider.getQuantityString(
                when (chargeLimitsStrategy) {
                    FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION ->
                        SharedResources.plurals.problems_limit_info_modal_left_lives
                    FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION ->
                        SharedResources.plurals.problems_limit_info_modal_left_problems
                },
                leftLimit,
                leftLimit
            )
        } else {
            null
        }

    private fun getResetInText(stepsLimitResetTime: Instant?): String? =
        if (stepsLimitResetTime != null) {
            resourceProvider.getString(
                SharedResources.strings.problems_limit_info_reset_in,
                sharedDateFormatter.formatHoursOrMinutesCountShort(stepsLimitResetTime - Clock.System.now())
            )
        } else {
            null
        }

    private fun getButtonText(): String =
        resourceProvider.getString(
            SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_problems_button
        )
}