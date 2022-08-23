import Foundation
import shared

enum NotificationsRegistrationService {
    static func requestAuthorization() {
        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound],
            completionHandler: { granted, error in
                print("NotificationsRegistrationService :: did complete request authorization granted = \(granted)")
                if let error = error {
                    print("NotificationsRegistrationService: did fail request authorization with error: \(error)")
                }

                DispatchQueue.main.async {
                    let analyticInteractor = AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
                    let analyticEvent = NotificationSystemNoticeHyperskillAnalyticEvent(
                        route: HyperskillAnalyticRoute.Home(),
                        isAllowed: granted
                    )
                    analyticInteractor.logEvent(event: analyticEvent, completionHandler: { _, _ in })
                }
            }
        )
    }
}
