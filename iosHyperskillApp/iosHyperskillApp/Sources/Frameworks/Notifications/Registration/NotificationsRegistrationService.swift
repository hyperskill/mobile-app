import Foundation

enum NotificationsRegistrationService {
    static func register() {
        UNUserNotificationCenter.current()
            .requestAuthorization(options: [.alert, .sound, .badge]) { _, error in
            if let error = error {
                print("Error requesting notifications autorization: \(error.localizedDescription)")
            }

            // TODO implement granted logic
            }
    }
}
