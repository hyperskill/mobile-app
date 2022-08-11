import UserNotifications

final class LocalNotificationsService {
    // MARK: - Getting Notifications -
    /// Returns a list of all notification requests that are scheduled and waiting to be delivered and
    /// a list of the app’s notifications that are still displayed in Notification Center.
    func getAllNotifications() async -> (pending: [UNNotificationRequest], delivered: [UNNotification]) {
        await (
            pending: UNUserNotificationCenter.current().pendingNotificationRequests(),
            delivered: UNUserNotificationCenter.current().deliveredNotifications()
        )
    }

    // MARK: - Cancelling Notifications -

    func removeAllNotifications() {
        UNUserNotificationCenter.current().removeAllPendingNotificationRequests()
        UNUserNotificationCenter.current().removeAllDeliveredNotifications()
    }

    func removeNotifications(identifiers: [String]) {
        if identifiers.isEmpty {
            return
        }

        removePendingNotificationRequests(identifiers: identifiers)
        removeDeliveredNotifications(identifiers: identifiers)
    }

    private func removeDeliveredNotifications(identifiers: [String]) {
        UNUserNotificationCenter.current().removeDeliveredNotifications(withIdentifiers: identifiers)
    }

    private func removePendingNotificationRequests(identifiers: [String]) {
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: identifiers)
    }

    // MARK: - Scheduling Notifications -

    func scheduleNotification(_ localNotification: LocalNotificationProtocol) async throws {
        guard let notificationTrigger = localNotification.trigger else {
            throw Error.badContentProvider
        }

        guard isFireDateValid(notificationTrigger.nextTriggerDate) else {
            throw Error.badFireDate
        }

        let request = UNNotificationRequest(
            identifier: localNotification.identifier,
            content: makeNotificationContent(localNotification: localNotification),
            trigger: notificationTrigger
        )

        try await UNUserNotificationCenter.current().add(request)
    }

    func isNotificationExists(identifier: String) async -> Bool {
        let (pending, delivered) = await getAllNotifications()

        if pending.contains(where: { $0.identifier == identifier }) {
            return true
        }

        if delivered.contains(where: { $0.request.identifier == identifier }) {
            return true
        }

        return false
    }

    private func makeNotificationContent(localNotification: LocalNotificationProtocol) -> UNNotificationContent {
        let content = UNMutableNotificationContent()
        content.title = localNotification.title
        content.body = localNotification.body
        content.sound = localNotification.sound

        var userInfo = localNotification.userInfo
        userInfo.merge(
            [
                PayloadKey.notificationName.rawValue: localNotification.identifier,
                PayloadKey.title.rawValue: localNotification.title,
                PayloadKey.body.rawValue: localNotification.body
            ],
            uniquingKeysWith: { (_, new) in new }
        )
        content.userInfo = userInfo

        return content
    }

    /// Checks that `fireDate` is valid.
    ///
    /// - Parameters:
    ///   - fireDate: The Date object to be checked.
    /// - Returns: `true` if the `fireDate` exists and it in the future, otherwise false.
    private func isFireDateValid(_ fireDate: Date?) -> Bool {
        if let fireDate = fireDate {
            return fireDate.compare(Date()) == .orderedDescending
        } else {
            return false
        }
    }

    // MARK: - Types -

    enum PayloadKey: String {
        case notificationName = "LocalNotificationServiceKey"
        case title
        case body
    }

    enum Error: Swift.Error {
        case badContentProvider
        case badFireDate
    }
}
