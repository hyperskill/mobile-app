import Foundation
import shared

struct DailyStudyReminderLocalNotification: LocalNotificationProtocol {
    var title: String

    var body: String

    var identifier: String { "DailyStudyReminderLocalNotification\(notificationNumber)" }

    var trigger: UNNotificationTrigger?

    private var startHour: Int

    private var notificationNumber: Int

    init(
        notificationDescritpion: NotificationDescription,
        startHour: Int,
        notificationNumber: Int
    ) {
        self.title = notificationDescritpion.title
        self.body = notificationDescritpion.text
        self.startHour = startHour
        self.notificationNumber = notificationNumber
        if let dateComponents = self.dateComponents {
            self.trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)
        }
    }

    fileprivate init(notificationNumber: Int) {
        self.init(
            notificationDescritpion: NotificationDescription(title: "", text: ""),
            startHour: 0,
            notificationNumber: notificationNumber
        )
    }

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
}

// MARK: - NotificationsService (DailyStudyReminderLocalNotification) -

extension NotificationsService {
    func scheduleDailyStudyReminderLocalNotifications(
        notificationDescriptions: [NotificationDescription],
        startHour: Int
    ) async {
        for (index, notificationDescription) in notificationDescriptions.enumerated() {
            let notification = DailyStudyReminderLocalNotification(
                notificationDescritpion: notificationDescription,
                startHour: startHour,
                notificationNumber: index + 1
            )
            await self.scheduleLocalNotification(notification)
        }
    }

    func removeDailyStudyReminderLocalNotifications(notificationsCount: Int) {
        self.removeLocalNotifications(
            identifiers: (1...notificationsCount)
                .map({ notificationNumber in
                    DailyStudyReminderLocalNotification(notificationNumber: notificationNumber).identifier})
        )
    }
}
