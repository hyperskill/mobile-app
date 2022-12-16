package org.hyperskill.app.profile.domain.analytic.streak_freeze

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

enum class StreakFreezeCardAnalyticAction(val analyticTarget: HyperskillAnalyticTarget) {
    GET_STREAK_FREEZE(HyperskillAnalyticTarget.GET_STREAK_FREEZE),
    STREAK_FREEZE_ICON(HyperskillAnalyticTarget.STREAK_FREEZE_ICON)
}