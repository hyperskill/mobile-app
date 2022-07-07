package org.hyperskill.app.streak.domain.model

enum class StreakStates(val representation: String) {
    COMPLETED("completed"),
    MANUAL_COMPLETED("manual_completed"),
    FROZEN("frozen")
}