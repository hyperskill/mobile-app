package org.hyperskill.app.problems_limit_info.view

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.State
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState

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
            title = getNoLimitsLeftTitle(),
            limitsDescription = null,
            animation = ViewState.Animation.NO_LIMITS_LEFT,
            leftLimitsText = getLeftLimitsText(0),
            resetInText = getResetInText(state.subscription.stepsLimitResetTime),
            unlockDescription = getUnlockDescription(),
            buttonText = getButtonText()
        )

    private fun getNoLimitsLeftTitle(): String =
        resourceProvider.getString(SharedResources.strings.problems_limit_info_modal_no_lives_left_title)

    private fun getUnlockDescription(): String =
        resourceProvider.getString(
            SharedResources.strings.problems_limit_info_modal_unlock_unlimited_lives_description
        )

    private fun getUserInitiatedViewState(state: State): ViewState =
        ViewState(
            title = getUserInitiatedTitle(
                leftLimit = state.subscription.stepsLimitLeft,
                limitTotal = state.subscription.stepsLimitTotal
            ),
            limitsDescription = getUserInitiatedLimitsDescription(),
            animation = getUserInitiatedAnimation(
                leftLimit = state.subscription.stepsLimitLeft,
                limitTotal = state.subscription.stepsLimitTotal
            ),
            leftLimitsText = getLeftLimitsText(state.subscription.stepsLimitLeft),
            resetInText = getResetInText(state.subscription.stepsLimitResetTime),
            unlockDescription = null,
            buttonText = getButtonText()
        )

    @Suppress("MagicNumber")
    private fun getUserInitiatedTitle(leftLimit: Int?, limitTotal: Int?): String =
        when (leftLimit) {
            null, 0 -> getNoLimitsLeftTitle()
            limitTotal ->
                resourceProvider.getString(SharedResources.strings.problems_limit_info_modal_full_limits_title)
            else ->
                resourceProvider.getString(SharedResources.strings.problems_limit_info_modal_part_spent_lives_title)
        }

    private fun getUserInitiatedLimitsDescription(): String =
        resourceProvider.getString(SharedResources.strings.problems_limit_info_modal_lives_description)

    @Suppress("MagicNumber")
    private fun getUserInitiatedAnimation(leftLimit: Int?, limitTotal: Int?): ViewState.Animation =
        when (leftLimit) {
            null, 0 -> ViewState.Animation.NO_LIMITS_LEFT
            limitTotal -> ViewState.Animation.FULL_LIMITS
            else -> ViewState.Animation.PARTIALLY_SPENT_LIMITS
        }

    private fun getLeftLimitsText(leftLimit: Int?): String? =
        if (leftLimit != null) {
            resourceProvider.getQuantityString(
                SharedResources.plurals.problems_limit_info_modal_left_lives,
                leftLimit,
                leftLimit
            )
        } else {
            null
        }

    private fun getResetInText(stepsLimitResetTime: Instant?): String? {
        val currentTime = Clock.System.now()
        return stepsLimitResetTime?.takeIf { it > currentTime }?.let {
            resourceProvider.getString(
                SharedResources.strings.problems_limit_info_reset_in,
                sharedDateFormatter.formatHoursOrMinutesCountShort(it - currentTime)
            )
        }
    }

    private fun getButtonText(): String =
        resourceProvider.getString(
            SharedResources.strings.problems_limit_reached_modal_unlock_unlimited_problems_button
        )
}