import Foundation
import shared
import UserNotifications

final class NotificationsRegistrationService {
    static let shared = NotificationsRegistrationService(
        analyticInteractor: AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
    )

    private let analyticInteractor: AnalyticInteractor

    private var requestAuthorizationContinuation: CheckedContinuation<Bool, Never>?

    private var applicationWasInBackground = false
    private var applicationOpenedSettings = false

    private init(analyticInteractor: AnalyticInteractor) {
        self.analyticInteractor = analyticInteractor
        addObservers()
    }

    // MARK: Public API

    func requestAuthorizationIfNeeded() async -> Bool {
        await withCheckedContinuation { continuation in
            assert(
                requestAuthorizationContinuation == nil,
                "NotificationsRegistrationService :: starting new authorization request while previous not finished"
            )
            requestAuthorizationContinuation = continuation
            doRequestAuthorizationFlow()
        }
    }

    // MARK: Private API

    private func doRequestAuthorizationFlow() {
        Task(priority: .userInitiated) {
            let permissionStatus = await NotificationPermissionStatus.current
            postCurrentPermissionStatus(permissionStatus)

            switch permissionStatus {
            case .notDetermined:
                if !Self.didShowSystemPermissionAlert {
                    logSystemNoticeShownEvent()
                }
                Self.didShowSystemPermissionAlert = true

                do {
                    let isGranted = try await UNUserNotificationCenter.current().requestAuthorization(
                        options: [.alert, .badge, .sound]
                    )

                    logSystemNoticeHiddenEvent(isAllowed: isGranted)

                    await resumeRequestAuthorizationContinuation(isGranted: isGranted)
                } catch {
                    #if DEBUG
                    print("NotificationsRegistrationService: did fail request authorization with error: \(error)")
                    #endif
                    await resumeRequestAuthorizationContinuation(isGranted: false)
                }
            case .denied:
                await requestAuthorizationInSettings()
            case .authorized:
                await resumeRequestAuthorizationContinuation(isGranted: true)
            }
        }
    }

    @MainActor
    private func resumeRequestAuthorizationContinuation(isGranted: Bool) {
        postCurrentPermissionStatus()

        requestAuthorizationContinuation?.resume(returning: isGranted)
        requestAuthorizationContinuation = nil

        applicationOpenedSettings = false
    }
}

// MARK: - NotificationsRegistrationService (NotificationCenter) -

extension NotificationsRegistrationService {
    private func addObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: UIApplication.shared
        )
    }

    @objc
    private func handleApplicationDidBecomeActive() {
        guard applicationWasInBackground else {
            return
        }

        applicationWasInBackground = false

        if applicationOpenedSettings {
            applicationOpenedSettings = false
            handleComeBackFromSettings()
        }
    }

    @objc
    private func handleApplicationDidEnterBackground() {
        applicationWasInBackground = true
    }

    private func postCurrentPermissionStatus(_ permissionStatus: NotificationPermissionStatus? = nil) {
        if let permissionStatus = permissionStatus {
            NotificationCenter.default.post(
                name: .notificationsRegistrationServiceDidUpdatePermissionStatus,
                object: permissionStatus
            )
        } else {
            Task {
                NotificationCenter.default.post(
                    name: .notificationsRegistrationServiceDidUpdatePermissionStatus,
                    object: await NotificationPermissionStatus.current
                )
            }
        }
    }
}

extension Foundation.Notification.Name {
    static let notificationsRegistrationServiceDidUpdatePermissionStatus = Foundation.Notification
        .Name("notificationsRegistrationServiceDidUpdatePermissionStatus")
}

// MARK: - NotificationsRegistrationService (Request Authorization via Settings) -

extension NotificationsRegistrationService {
    @MainActor
    private func requestAuthorizationInSettings() {
        guard let currentPresentedViewController = SourcelessRouter().currentPresentedViewController() else {
            return resumeRequestAuthorizationContinuation(isGranted: false)
        }

        let alert = UIAlertController(
            title: NSLocalizedString("DeniedNotificationsSettingsAlertTitle", comment: ""),
            message: NSLocalizedString("DeniedNotificationsSettingsAlertMessage", comment: ""),
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: NSLocalizedString("DeniedNotificationsSettingsAlertPositiveActionTitle", comment: ""),
                style: .default,
                handler: { _ in
                    self.openSettings()
                }
            )
        )
        alert.addAction(
            UIAlertAction(
                title: NSLocalizedString("DeniedNotificationsSettingsAlertNegativeActionTitle", comment: ""),
                style: .cancel,
                handler: { _ in
                    self.resumeRequestAuthorizationContinuation(isGranted: false)
                }
            )
        )

        currentPresentedViewController.present(alert, animated: true)
    }

    @MainActor
    private func openSettings() {
        guard let settingsURL = URL(string: UIApplication.openSettingsURLString) else {
            return resumeRequestAuthorizationContinuation(isGranted: false)
        }

        if UIApplication.shared.canOpenURL(settingsURL) {
            applicationOpenedSettings = true
            UIApplication.shared.open(settingsURL)
        } else {
            resumeRequestAuthorizationContinuation(isGranted: false)
        }
    }

    private func handleComeBackFromSettings() {
        Task(priority: .userInitiated) {
            let permissionStatus = await NotificationPermissionStatus.current
            switch permissionStatus {
            case .notDetermined:
                // Impossible case
                await resumeRequestAuthorizationContinuation(isGranted: false)
            case .denied:
                await resumeRequestAuthorizationContinuation(isGranted: false)
            case .authorized:
                await resumeRequestAuthorizationContinuation(isGranted: true)
            }
        }
    }
}

// MARK: - NotificationsRegistrationService (Analytic) -

extension NotificationsRegistrationService {
    private func logSystemNoticeShownEvent() {
        DispatchQueue.main.async {
            let event = NotificationSystemNoticeShownHyperskillAnalyticEvent(route: HyperskillAnalyticRoute.Home())
            self.analyticInteractor.logEvent(event: event)
        }
    }

    private func logSystemNoticeHiddenEvent(isAllowed: Bool) {
        DispatchQueue.main.async {
            let event = NotificationSystemNoticeHiddenHyperskillAnalyticEvent(
                route: HyperskillAnalyticRoute.Home(),
                isAllowed: isAllowed
            )
            self.analyticInteractor.logEvent(event: event)
        }
    }
}

// MARK: - NotificationsRegistrationService (UserDefaults) -

extension NotificationsRegistrationService {
    private static let didShowSystemPermissionAlertKey = "didShowSystemPermissionAlertKey"

    private static var didShowSystemPermissionAlert: Bool {
        get {
            UserDefaults.standard.bool(forKey: didShowSystemPermissionAlertKey)
        }
        set {
            UserDefaults.standard.set(newValue, forKey: didShowSystemPermissionAlertKey)
        }
    }
}
