import Foundation
import shared

enum NotificationsRegistrationService {
    static func requestAuthorization() {
        logSystemNoticeShownEvent()

        UNUserNotificationCenter.current().requestAuthorization(
            options: [.alert, .badge, .sound],
            completionHandler: { granted, error in
                print("NotificationsRegistrationService :: did complete request authorization granted = \(granted)")
                if let error = error {
                    print("NotificationsRegistrationService: did fail request authorization with error: \(error)")
                }
                logSystemNoticeHiddenEvent(isAllowed: granted)
            }
        )
    }

    // MARK: Analytic

    private static func logSystemNoticeShownEvent() {
        DispatchQueue.main.async {
            let analyticInteractor = AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
            let analyticEvent = NotificationSystemNoticeShownHyperskillAnalyticEvent(
                route: HyperskillAnalyticRoute.Home()
            )
            analyticInteractor.logEvent(event: analyticEvent, completionHandler: { _, _ in })
        }
    }

    private static func logSystemNoticeHiddenEvent(isAllowed: Bool) {
        DispatchQueue.main.async {
            let analyticInteractor = AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
            let analyticEvent = NotificationSystemNoticeHiddenHyperskillAnalyticEvent(
                route: HyperskillAnalyticRoute.Home(),
                isAllowed: isAllowed
            )
            analyticInteractor.logEvent(event: analyticEvent, completionHandler: { _, _ in })
        }
    }
}
