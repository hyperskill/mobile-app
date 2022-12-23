package org.hyperskill.app.profile.domain.analytic.streak_freeze

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

enum class StreakFreezeModalAnalyticAction(val analyticTarget: HyperskillAnalyticTarget) {
    GET_IT(HyperskillAnalyticTarget.GET_IT),
    CONTINUE_LEARNING(HyperskillAnalyticTarget.CONTINUE_LEARNING)
}