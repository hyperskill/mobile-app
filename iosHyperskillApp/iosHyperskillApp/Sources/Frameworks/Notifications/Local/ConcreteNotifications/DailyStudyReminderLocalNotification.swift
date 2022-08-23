import Foundation
import shared

struct DailyStudyReminderLocalNotification: LocalNotificationProtocol {
    fileprivate static let identifierPrefix = "DailyStudyReminderLocalNotification"

    var title: String

    var body: String

    var trigger: UNNotificationTrigger?

    private var startHour: Int

    private var notificationNumber: Int

    var identifier: String { "\(Self.identifierPrefix)-\(notificationNumber)" }

    private var dateComponents: DateComponents? {
        guard let date = Calendar.current.date(
            byAdding: .day,
            value: notificationNumber,
            to: Date()
        ) else {
            return nil
        }

        var reminderDateComponents = Calendar.current.dateComponents([.hour, .weekday], from: date)

        reminderDateComponents.hour = self.startHour

        return reminderDateComponents
    }

    init(
        notificationDescription: NotificationDescriptionPlainObject,
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
    fileprivate static let dailyStudyRemindersCount = 7

    func scheduleDailyStudyReminderLocalNotifications() {
        guard self.notificationInteractor.isDailyStudyRemindersEnabled() else {
            return
        }

        let notificationDescriptions = self.notificationInteractor
            .getShuffledDailyStudyRemindersNotificationDescriptions()
            .prefix(Self.dailyStudyRemindersCount)
            .map(NotificationDescriptionPlainObject.init(notificationDescription:))

        assert(notificationDescriptions.count == Self.dailyStudyRemindersCount)

        let startHour = Int(self.notificationInteractor.getDailyStudyRemindersIntervalStartHour())

        Task {
            await self.internalRemoveDailyStudyReminderLocalNotifications()

            for (index, notificationDescription) in notificationDescriptions.enumerated() {
                let notification = DailyStudyReminderLocalNotification(
                    notificationDescription: notificationDescription,
                    startHour: startHour,
                    notificationNumber: index + 1
                )
                await self.scheduleLocalNotification(notification, removeIdentical: false)
            }
        }
    }

    func removeDailyStudyReminderLocalNotifications() {
        Task {
            await self.internalRemoveDailyStudyReminderLocalNotifications()
        }
    }

    private func internalRemoveDailyStudyReminderLocalNotifications() async {
        await self.removeLocalNotifications { identifier in
            identifier.starts(with: DailyStudyReminderLocalNotification.identifierPrefix)
        }
    }
}
