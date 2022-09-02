import Foundation
import shared

enum NotificationsRegistrationService {
    static func requestAuthorization(grantedHandler: @escaping (Bool) -> Void) {
        if !didShowDefaultPermissionAlert {
            logSystemNoticeShownEvent()
        }

        didShowDefaultPermissionAlert = true

        Task {
            let notificationPermissionStatus = await NotificationPermissionStatus.current

            switch notificationPermissionStatus {
            case .notDetermined:
                do {
                    let isGranted = try await UNUserNotificationCenter.current().requestAuthorization(
                        options: [.alert, .badge, .sound]
                    )

                    logSystemNoticeHiddenEvent(isAllowed: isGranted)

                    await MainActor.run {
                        grantedHandler(isGranted)
                    }
                } catch {
                    print("NotificationsRegistrationService: did fail request authorization with error: \(error)")
                    await MainActor.run {
                        grantedHandler(false)
                    }
                }
            case .denied:
                await MainActor.run {
                    grantedHandler(false)
                    if let settingsURL = URL(string: UIApplication.openSettingsURLString) {
                        UIApplication.shared.open(settingsURL)
                    }
                }
            case .authorized:
                await MainActor.run {
                    grantedHandler(true)
                }
            }
        }
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

// MARK: - NotificationsRegistrationService (UserDefaults) -

extension NotificationsRegistrationService {
    private static let didShowDefaultPermissionAlertKey = "didShowDefaultPermissionAlertKey"

    private static var didShowDefaultPermissionAlert: Bool {
        get {
            UserDefaults.standard.bool(forKey: didShowDefaultPermissionAlertKey)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: didShowDefaultPermissionAlertKey)
        }
    }
}
