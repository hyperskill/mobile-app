import Foundation
import shared

struct DailyStudyReminderLocalNotification: LocalNotificationProtocol {
    fileprivate static let identifierPrefix = "DailyStudyReminderLocalNotification"

    var title: String

    var body: String

    var trigger: UNNotificationTrigger?

    private var startHour: Int

    private var notificationNumber: Int

    var identifier: String { "\(notificationNumber)" }

    private var dateComponents: DateComponents? {
        guard let date = Calendar.current.date(
            byAdding: .day,
            value: notificationNumber,
            to: Date()
        ) else {
            return nil
        }

        var reminderDateComponents = Calendar.current.dateComponents([.hour, .day, .month, .year], from: date)

        reminderDateComponents.hour = self.startHour

        return reminderDateComponents
    }

    init(
        notificationDescription: NotificationDescription,
        startHour: Int,
        notificationNumber: Int
    ) {
        self.title = notificationDescription.title
        self.body = notificationDescription.text
        self.startHour = startHour
        self.notificationNumber = notificationNumber
        if let dateComponents = self.dateComponents {
            self.trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)
        }
    }
}

// MARK: - NotificationsService (DailyStudyReminderLocalNotification) -

extension NotificationsService {
    func scheduleDailyStudyReminderLocalNotifications() {
        if self.notificationInteractor.isDailyStudyRemindersEnabled() {
            let notificationDescriptions = self.notificationInteractor
                .getShuffledDailyStudyRemindersNotificationDescriptions()
            Task {
                for (index, notificationDescription) in notificationDescriptions.enumerated() {
                    let notification = DailyStudyReminderLocalNotification(
                        notificationDescription: notificationDescription,
                        startHour: Int(self.notificationInteractor.getDailyStudyRemindersIntervalStartHour()),
                        notificationNumber: index + 1
                    )
                    await self.scheduleLocalNotification(notification)
                }
            }
        }
    }

    func removeDailyStudyReminderLocalNotifications() {
        Task {
            await self.removeLocalNotifications { identifier in
                identifier.starts(with: DailyStudyReminderLocalNotification.identifierPrefix)
            }
        }
    }
}
