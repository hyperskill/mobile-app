import Foundation
import shared

struct DailyStudyReminderLocalNotification: LocalNotificationProtocol {
    fileprivate static let identifierPrefix = NotificationsService.NotificationType.dailyStudyReminder.rawValue

    var title: String

    var body: String

    var trigger: UNNotificationTrigger?

    private var startHour: Int

    private let notificationID: Int
    private let notificationNumber: Int

    var userInfo: [AnyHashable: Any] {
        [
            NotificationsService.PayloadKey.id.rawValue: notificationID,
            NotificationsService.PayloadKey.type.rawValue: NotificationsService.NotificationType
                .dailyStudyReminder.rawValue
        ]
    }

    var identifier: String { "\(Self.identifierPrefix)-\(notificationNumber)" }

    fileprivate var fireDate: Date? {
        guard let nextDayDate = Calendar.current.date(
            byAdding: .day,
            value: notificationNumber,
            to: Date()
        ) else {
            return nil
        }

        return Calendar.current.date(bySettingHour: startHour, minute: 0, second: 0, of: nextDayDate)
    }

    private var dateComponents: DateComponents? {
        guard let fireDate = fireDate else {
            return nil
        }

        var reminderDateComponents = Calendar.current.dateComponents([.hour, .weekday], from: fireDate)
        reminderDateComponents.hour = startHour

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
        self.notificationID = notificationDescription.id
        self.notificationNumber = notificationNumber

        if let dateComponents = self.dateComponents {
            self.trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)
        }
    }
}

// MARK: - NotificationsService (DailyStudyReminderLocalNotification) -

extension NotificationsService {
    fileprivate static let dailyStudyRemindersCount = 7

    private static let iso8601DateFormatter = ISO8601DateFormatter()

    func scheduleDailyStudyReminderLocalNotifications(
        analyticRoute: HyperskillAnalyticRoute = HyperskillAnalyticRoute.Home()
    ) {
        guard self.notificationInteractor.isDailyStudyRemindersEnabled() else {
            return
        }

        let notificationDescriptions = notificationInteractor
            .getShuffledDailyStudyRemindersNotificationDescriptions()
            .prefix(Self.dailyStudyRemindersCount)
            .map(NotificationDescriptionPlainObject.init(notificationDescription:))

        assert(notificationDescriptions.count == Self.dailyStudyRemindersCount)

        let startHour = Int(notificationInteractor.getDailyStudyRemindersIntervalStartHour())

        Task {
            await internalRemoveDailyStudyReminderLocalNotifications()

            for (index, notificationDescription) in notificationDescriptions.enumerated() {
                let notification = DailyStudyReminderLocalNotification(
                    notificationDescription: notificationDescription,
                    startHour: startHour,
                    notificationNumber: index + 1
                )
                await scheduleLocalNotification(notification, removeIdentical: false)

                let plannedAtISO8601 = notification.fireDate != nil
                    ? Self.iso8601DateFormatter.string(from: notification.fireDate.require())
                    : nil

                await logDailyStudyReminderShownEvent(
                    analyticRoute: analyticRoute,
                    notificationID: notificationDescription.id,
                    plannedAtISO8601: plannedAtISO8601
                )
            }
        }
    }

    func removeDailyStudyReminderLocalNotifications() {
        Task {
            await internalRemoveDailyStudyReminderLocalNotifications()
        }
    }

    private func internalRemoveDailyStudyReminderLocalNotifications() async {
        await removeLocalNotifications { identifier in
            identifier.starts(with: DailyStudyReminderLocalNotification.identifierPrefix)
        }
    }

    @MainActor
    private func logDailyStudyReminderShownEvent(
        analyticRoute: HyperskillAnalyticRoute,
        notificationID: Int,
        plannedAtISO8601: String?
    ) {
        let event = NotificationDailyStudyReminderShownHyperskillAnalyticEvent(
            route: analyticRoute,
            notificationId: Int32(notificationID),
            plannedAtISO8601: plannedAtISO8601
        )
        analyticInteractor.logEvent(event: event, completionHandler: { _, _ in })
    }
}
