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
        self.notificationNumber = notificationNumber
        if let dateComponents = self.dateComponents {
            self.trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)
        }
    }
}

// MARK: - NotificationsService (DailyStudyReminderLocalNotification) -

extension NotificationsService {
    fileprivate static let dailyStudyRemindersCount = 7

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

            let isNotificationPermissionGranted = await NotificationPermissionStatus.current.isRegistered

            for (index, notificationDescription) in notificationDescriptions.enumerated() {
                let notification = DailyStudyReminderLocalNotification(
                    notificationDescription: notificationDescription,
                    startHour: startHour,
                    notificationNumber: index + 1
                )
                await scheduleLocalNotification(notification, removeIdentical: false)

                await logDailyStudyReminderShownEvent(
                    analyticRoute: analyticRoute,
                    notificationID: notificationDescription.id,
                    isNotificationPermissionGranted: isNotificationPermissionGranted
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
        isNotificationPermissionGranted: Bool
    ) {
        let event = NotificationDailyStudyReminderShownHyperskillAnalyticEvent(
            route: analyticRoute,
            isNotificationPermissionGranted: isNotificationPermissionGranted,
            notificationId: Int32(notificationID)
        )
        analyticInteractor.logEvent(event: event, completionHandler: { _, _ in })
    }
}
