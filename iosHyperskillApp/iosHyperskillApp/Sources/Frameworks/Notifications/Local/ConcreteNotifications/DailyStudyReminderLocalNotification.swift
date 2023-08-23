import Foundation

// MARK: - NotificationsService (DailyStudyReminderLocalNotification) -

extension NotificationsService {
    func removeDailyStudyReminderLocalNotifications() {
        Task {
            await removeLocalNotifications { identifier in
                identifier.starts(with: NotificationsService.NotificationType.dailyStudyReminder.rawValue)
            }
        }
    }
}
