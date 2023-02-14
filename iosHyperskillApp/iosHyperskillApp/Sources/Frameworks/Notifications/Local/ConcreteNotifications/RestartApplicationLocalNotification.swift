import Foundation
import UserNotifications

struct RestartApplicationLocalNotification: LocalNotificationProtocol {
    var title: String { Strings.DebugMenu.RestartApplication.LocalNotification.title }

    var body: String { Strings.DebugMenu.RestartApplication.LocalNotification.body }

    var identifier: String { NotificationsService.NotificationType.restartApplication.rawValue }

    var trigger: UNNotificationTrigger? {
        UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false)
    }
}

// MARK: - NotificationsService (RestartApplicationLocalNotification) -

extension NotificationsService {
    func scheduleRestartApplicationLocalNotification() async {
        await scheduleLocalNotification(RestartApplicationLocalNotification())
    }
}
