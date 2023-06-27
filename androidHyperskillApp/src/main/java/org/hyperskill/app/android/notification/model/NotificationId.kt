package org.hyperskill.app.android.notification.model

enum class NotificationId(val notificationId: Long) {
    DailyStudyReminder(0),
    StreakThree(1),
    StreakWeek(2),
    StreakRecordStart(3),
    StreakRecordNear(4),
    StreakRecordComplete(5),
    LearnTopic(6),
    Repetition(7),
    StepStreakFreezeTokenUsed(8),
    StreakFreezeOnboarding(9),
    StreakNew(10),
    RemindShort(11),
    RemindMedium(12),

    Unknown(Long.MIN_VALUE)
}