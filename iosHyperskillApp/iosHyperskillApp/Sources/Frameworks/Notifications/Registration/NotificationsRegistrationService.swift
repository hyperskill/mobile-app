import Foundation

enum NotificationsRegistrationService {
    static func requestAuthorization() {
        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound],
            completionHandler: { granted, error in
                print("NotificationsRegistrationService :: did complete request authorization granted = \(granted)")
                if let error = error {
                    print("NotificationsRegistrationService: did fail request authorization with error: \(error)")
                }
            }
        )
    }
}
