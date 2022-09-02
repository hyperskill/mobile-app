import Foundation
import shared

final class NotificationPermissionStatusSettingsObserver {
    private let notificationInteractor: NotificationInteractor

    private var didTransitionToBackground = false

    init(notificationInteractor: NotificationInteractor) {
        self.notificationInteractor = notificationInteractor
    }

    func startObserving() {
        Task {
            let permissionStatus = await NotificationPermissionStatus.current

            let isFirstPermissionAccess = UserDefaults.standard.string(
                forKey: NotificationPermissionStatusSettingsObserver.notificationPermissionStatusKey
            ) == nil
            let isPermissionStatusChanged = notificationPermissionStatus != permissionStatus

            if !isFirstPermissionAccess && isPermissionStatusChanged {
                await postNotificationPermissionStatusDidChangeNotification(permissionStatus)
            }

            notificationPermissionStatus = permissionStatus
            await addObservers()
        }
    }

    // MARK: Private API

    @objc
    private func handleApplicationDidBecomeActive() {
        Task {
            let permissionStatus = await NotificationPermissionStatus.current

            if notificationPermissionStatus != permissionStatus {
                await postNotificationPermissionStatusDidChangeNotification(permissionStatus)
            }

            didTransitionToBackground = false
            notificationPermissionStatus = permissionStatus
        }
    }

    @objc
    private func handleApplicationWillResignActive() {
        didTransitionToBackground = true
        Task {
            notificationPermissionStatus = await NotificationPermissionStatus.current
        }
    }

    @objc
    private func onPermissionStatusUpdate(_ notification: Foundation.Notification) {
        guard let permissionStatus = notification.object as? NotificationPermissionStatus else {
            return
        }

        // Wait for `UIApplicationWillEnterForeground` event and after that allow status updates.
        if !didTransitionToBackground {
            notificationPermissionStatus = permissionStatus
        }
    }

    @MainActor
    private func addObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationWillResignActive),
            name: UIApplication.willResignActiveNotification,
            object: UIApplication.shared
        )

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(onPermissionStatusUpdate(_:)),
            name: .notificationsRegistrationServiceDidUpdatePermissionStatus,
            object: nil
        )
    }
}

// MARK: - NotificationPermissionStatusSettingsObserver (UserDefaults) -

extension NotificationPermissionStatusSettingsObserver {
    private static let notificationPermissionStatusKey = "notificationPermissionStatusKey"

    private var notificationPermissionStatus: NotificationPermissionStatus {
        get {
            if let stringValue = UserDefaults.standard.string(forKey: Self.notificationPermissionStatusKey) {
                return NotificationPermissionStatus(rawValue: stringValue) ?? .notDetermined
            } else {
                return .notDetermined
            }
        }
        set {
            UserDefaults.standard.set(newValue.rawValue, forKey: Self.notificationPermissionStatusKey)
            DispatchQueue.main.async {
                self.notificationInteractor.setNotificationsPermissionGranted(isGranted: newValue.isRegistered)
            }
        }
    }
}

// MARK: - NotificationPermissionStatusSettingsObserver (NotificationCenter) -

extension Foundation.Notification.Name {
    static let notificationPermissionStatusDidChange = Foundation.Notification
        .Name("notificationPermissionStatusDidChange")
}

extension NotificationPermissionStatusSettingsObserver {
    @MainActor
    private func postNotificationPermissionStatusDidChangeNotification(
        _ permissionStatus: NotificationPermissionStatus
    ) {
        NotificationCenter.default.post(
            name: .notificationPermissionStatusDidChange,
            object: permissionStatus
        )
    }
}

// MARK: - NotificationPermissionStatusSettingsObserver (Default Instance) -

extension NotificationPermissionStatusSettingsObserver {
    static var `default`: NotificationPermissionStatusSettingsObserver {
        NotificationPermissionStatusSettingsObserver(
            notificationInteractor: AppGraphBridge.sharedAppGraph.buildNotificationComponent().notificationInteractor
        )
    }
}
