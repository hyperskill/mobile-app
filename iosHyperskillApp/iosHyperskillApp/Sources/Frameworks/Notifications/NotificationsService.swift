import Foundation
import shared
import UserNotifications

final class NotificationsService {
    private let localNotificationsService: LocalNotificationsService

    let notificationInteractor: NotificationInteractor =
        AppGraphBridge.sharedAppGraph.buildNotificationComponent().notificationInteractor

    init(localNotificationsService: LocalNotificationsService = LocalNotificationsService()) {
        self.localNotificationsService = localNotificationsService
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
}
