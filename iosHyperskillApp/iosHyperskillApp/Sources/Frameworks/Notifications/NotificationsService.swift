import Foundation
import shared
import UserNotifications

final class NotificationsService {
    private let localNotificationsService: LocalNotificationsService

    init(localNotificationsService: LocalNotificationsService = LocalNotificationsService()) {
        self.localNotificationsService = localNotificationsService
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
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
            print("Failed schedule local notification with error: \(error)")
        }
    }

    func removeAllLocalNotifications() {
        self.localNotificationsService.removeAllNotifications()
    }

    func removeLocalNotifications(identifiers: [String]) {
        self.localNotificationsService.removeNotifications(identifiers: identifiers)
    }
}
