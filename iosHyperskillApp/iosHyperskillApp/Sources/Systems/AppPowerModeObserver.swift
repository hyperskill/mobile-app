import Foundation

final class AppPowerModeObserver {
    static let shared = AppPowerModeObserver()

    private(set) var isLowPowerModeEnabled: Bool {
        didSet {
            self.updateProgressHUDHapticsEnabled()
        }
    }

    private init() {
        self.isLowPowerModeEnabled = ProcessInfo.processInfo.isLowPowerModeEnabled
        updateProgressHUDHapticsEnabled()
    }

    private func updateProgressHUDHapticsEnabled() {
        // swiftlint:disable:next unowned_variable_capture
        DispatchQueue.main.async { [unowned self] in
            ProgressHUD.setHapticsEnabled(!self.isLowPowerModeEnabled)
        }
    }

    func observe() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleNSProcessInfoPowerStateDidChange(notification:)),
            name: .NSProcessInfoPowerStateDidChange,
            object: nil
        )
    }

    @objc
    private func handleNSProcessInfoPowerStateDidChange(notification: Notification) {
        guard let processInfo = notification.object as? ProcessInfo else {
            return assertionFailure("Expected ProcessInfo")
        }

        isLowPowerModeEnabled = processInfo.isLowPowerModeEnabled
    }
}
