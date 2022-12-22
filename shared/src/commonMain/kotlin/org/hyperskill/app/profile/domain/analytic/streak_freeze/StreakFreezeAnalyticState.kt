package org.hyperskill.app.profile.domain.analytic.streak_freeze

enum class StreakFreezeAnalyticState(val stringValue: String) {
    CAN_BUY("can_buy"),
    NOT_ENOUGH_GEMS("not_enough_gems"),
    ALREADY_HAVE("already_have")
}