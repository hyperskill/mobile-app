import Foundation
import shared
import UserNotifications

final class NotificationsService {
    typealias NotificationUserInfo = [AnyHashable: Any]

    private let localNotificationsService: LocalNotificationsService

    let notificationInteractor: NotificationInteractor
    let analyticInteractor: AnalyticInteractor
    let pushNotificationsInteractor: PushNotificationsInteractor

    init(
        localNotificationsService: LocalNotificationsService = LocalNotificationsService(),
        notificationInteractor: NotificationInteractor =
            AppGraphBridge.sharedAppGraph.buildNotificationComponent().notificationInteractor,
        analyticInteractor: AnalyticInteractor = AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor,
        pushNotificationsInteractor: PushNotificationsInteractor =
            AppGraphBridge.sharedAppGraph.buildPushNotificationsComponent().pushNotificationsInteractor
    ) {
        self.localNotificationsService = localNotificationsService
        self.notificationInteractor = notificationInteractor
        self.analyticInteractor = analyticInteractor
        self.pushNotificationsInteractor = pushNotificationsInteractor
    }

    func handleLaunchOptions(_ launchOptions: [UIApplication.LaunchOptionsKey: Any]?) {
        if let localNotification = launchOptions?[.localNotification] as? UILocalNotification {
            handleLocalNotification(with: localNotification.userInfo)
        } else if let remoteNotificationUserInfo = launchOptions?[.remoteNotification] as? NotificationUserInfo {
            handleRemoteNotification(with: remoteNotificationUserInfo)
        }
    }

    enum NotificationType: String {
        case dailyStudyReminder = "DailyStudyReminder"
        case restartApplication = "RestartApplication"
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
            #if DEBUG
            print("NotificationsService :: failed schedule local notification with error: \(error)")
            #endif
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

// MARK: - NotificationsService (RemoteNotifications) -

extension NotificationsService {
    func handleRemoteNotification(with userInfo: NotificationUserInfo) {
        #if DEBUG
        print("NotificationsService: did receive remote notification with userInfo: \(userInfo)")

        if let aps = userInfo["aps"] {
            if let apsDict = aps as? [String: Any] {
                if let pushNotificationData = pushNotificationsInteractor.parsePushNotificationData(rawNotificationData: apsDict) {
                    print("NotificationsService: did receive remote notification with pushNotificationData: \(pushNotificationData)")
                }
            }
        }
        if let data = try? JSONSerialization.data(withJSONObject: userInfo, options: [.prettyPrinted]),
           let string = String(data: data, encoding: .utf8) {
            print("NotificationsService: did receive remote notification with JSON string: \(string)")
        }
        #endif
    }
}
