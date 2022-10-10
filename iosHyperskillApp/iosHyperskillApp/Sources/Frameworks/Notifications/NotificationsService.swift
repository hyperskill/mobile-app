import Foundation
import shared
import UserNotifications

final class NotificationsService {
    typealias NotificationUserInfo = [AnyHashable: Any]

    private let localNotificationsService: LocalNotificationsService

    let notificationInteractor: NotificationInteractor
    let analyticInteractor: AnalyticInteractor

    init(
        localNotificationsService: LocalNotificationsService = LocalNotificationsService(),
        notificationInteractor: NotificationInteractor =
            AppGraphBridge.sharedAppGraph.buildNotificationComponent().notificationInteractor,
        analyticInteractor: AnalyticInteractor = AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
    ) {
        self.localNotificationsService = localNotificationsService
        self.notificationInteractor = notificationInteractor
        self.analyticInteractor = analyticInteractor
    }

    func handleLaunchOptions(_ launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        guard let localNotification = launchOptions?[.localNotification] as? UILocalNotification else {
            return
        }

        handleLocalNotification(with: localNotification.userInfo)
    }

    enum NotificationType: String {
        case dailyStudyReminder = "DailyStudyReminder"
    }
}

// MARK: - NotificationsService (LocalNotifications) -

extension NotificationsService {
    func scheduleLocalNotification(_ localNotification: LocalNotificationProtocol, removeIdentical: Bool = true) async {
        if removeIdentical {
            self.removeLocalNotifications(identifiers: [localNotification.identifier])
        }

        do {
            try await self.localNotificationsService.scheduleNotification(localNotification)
        } catch {
            print("NotificationsService :: failed schedule local notification with error: \(error)")
        }
    }

    func removeAllLocalNotifications() {
        self.localNotificationsService.removeAllNotifications()
    }

    func removeLocalNotifications(identifiers: [String]) {
        self.localNotificationsService.removeNotifications(identifiers: identifiers)
    }

    func removeLocalNotifications(matching condition: (String) -> Bool) async {
        await self.localNotificationsService.removeNotifications(matching: condition)
    }

    func handleLocalNotification(with userInfo: NotificationUserInfo?) {
        #if DEBUG
        print("NotificationsService :: did receive local notification with info: \(userInfo ?? [:])")
        #endif
        reportReceivedLocalNotification(with: userInfo)
        routeLocalNotification(with: userInfo)
    }

    // MARK: Private Helpers

    private func reportReceivedLocalNotification(with userInfo: NotificationUserInfo?) {
        guard let userInfo = userInfo,
              let notificationName = userInfo[
                LocalNotificationsService.PayloadKey.notificationName.rawValue
              ] as? String else {
            return
        }

        if notificationName.localizedCaseInsensitiveContains(NotificationType.dailyStudyReminder.rawValue) {
            guard let notificationID = userInfo[PayloadKey.id.rawValue] as? Int else {
                return
            }

            DispatchQueue.main.async {
                self.logDailyStudyReminderClickedEvent(notificationID: notificationID)
            }
        }
    }

    private func logDailyStudyReminderClickedEvent(notificationID: Int) {
        let event = NotificationDailyStudyReminderClickedHyperskillAnalyticEvent(notificationId: Int32(notificationID))
        analyticInteractor.logEvent(event: event)
    }

    private func routeLocalNotification(with userInfo: NotificationUserInfo?) {
        func route(to tab: TabBarRouter.Tab) {
            DispatchQueue.main.async {
                TabBarRouter(tab: tab).route()
            }
        }

        guard let userInfo = userInfo,
              let notificationName = userInfo[
                LocalNotificationsService.PayloadKey.notificationName.rawValue
              ] as? String else {
            return
        }

        if notificationName.localizedCaseInsensitiveContains(NotificationType.dailyStudyReminder.rawValue) {
            route(to: .home)
        }
    }

    enum PayloadKey: String {
        case type
        case id
    }
}
