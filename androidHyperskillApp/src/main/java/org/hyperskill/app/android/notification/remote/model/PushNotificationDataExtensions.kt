package org.hyperskill.app.android.notification.remote.model

import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.model.NotificationId
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.notification.remote.domain.model.PushNotificationType

val PushNotificationData.id: NotificationId
    get() = when (typeEnum) {
        PushNotificationType.STREAK_THREE -> NotificationId.StreakThree
        PushNotificationType.STREAK_WEEK -> NotificationId.StreakWeek
        PushNotificationType.STREAK_RECORD_START -> NotificationId.StreakRecordStart
        PushNotificationType.STREAK_RECORD_NEAR -> NotificationId.StreakRecordNear
        PushNotificationType.STREAK_RECORD_COMPLETE -> NotificationId.StreakRecordComplete
        PushNotificationType.LEARN_TOPIC -> NotificationId.LearnTopic
        PushNotificationType.REPETITION -> NotificationId.Repetition
        PushNotificationType.STEP_STREAK_FREEZE_TOKEN_USED -> NotificationId.StepStreakFreezeTokenUsed
        PushNotificationType.STREAK_FREEZE_ONBOARDING -> NotificationId.StreakFreezeOnboarding
        PushNotificationType.STREAK_NEW -> NotificationId.StreakNew
        PushNotificationType.REMIND_SHORT -> NotificationId.RemindShort
        PushNotificationType.REMIND_MEDIUM -> NotificationId.RemindMedium
        PushNotificationType.USER_BADGE_UPDATED -> NotificationId.UserBadgeUpdated
        PushNotificationType.USER_BADGE_UNLOCKED -> NotificationId.UserBadgeUnlocked
        PushNotificationType.DAILY_REMINDER -> NotificationId.DailyStudyReminder
        PushNotificationType.UNKNOWN -> NotificationId.Unknown
    }

val PushNotificationData.channel: HyperskillNotificationChannel
    get() = when (typeEnum) {
        PushNotificationType.STREAK_THREE,
        PushNotificationType.STREAK_WEEK,
        PushNotificationType.STREAK_RECORD_START,
        PushNotificationType.STREAK_RECORD_NEAR,
        PushNotificationType.STREAK_RECORD_COMPLETE,
        PushNotificationType.STREAK_NEW -> HyperskillNotificationChannel.StreakBoosters

        PushNotificationType.STEP_STREAK_FREEZE_TOKEN_USED,
        PushNotificationType.STREAK_FREEZE_ONBOARDING -> HyperskillNotificationChannel.StreakSavers

        PushNotificationType.LEARN_TOPIC,
        PushNotificationType.REPETITION,
        PushNotificationType.REMIND_SHORT,
        PushNotificationType.REMIND_MEDIUM,
        PushNotificationType.USER_BADGE_UNLOCKED,
        PushNotificationType.USER_BADGE_UPDATED -> HyperskillNotificationChannel.RegularLearningReminders

        PushNotificationType.DAILY_REMINDER -> HyperskillNotificationChannel.DailyReminder

        PushNotificationType.UNKNOWN -> HyperskillNotificationChannel.Other
    }