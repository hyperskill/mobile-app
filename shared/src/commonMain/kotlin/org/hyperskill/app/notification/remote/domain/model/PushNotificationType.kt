package org.hyperskill.app.notification.remote.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PushNotificationType {
    STREAK_THREE,
    STREAK_WEEK,
    STREAK_RECORD_START,
    STREAK_RECORD_NEAR,
    STREAK_RECORD_COMPLETE,
    LEARN_TOPIC,
    REPETITION,
    STEP_STREAK_FREEZE_TOKEN_USED,
    STREAK_FREEZE_ONBOARDING,
    STREAK_NEW,
    REMIND_SHORT,
    REMIND_MEDIUM,
    USER_BADGE_UPDATED,
    USER_BADGE_UNLOCKED,

    UNKNOWN
}