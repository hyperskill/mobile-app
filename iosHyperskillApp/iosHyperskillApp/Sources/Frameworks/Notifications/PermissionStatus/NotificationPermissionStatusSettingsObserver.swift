import Foundation

final class NotificationPermissionStatusSettingsObserver {
    private var didTransitionToBackground = false

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
