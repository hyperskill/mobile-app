import SVProgressHUD
import UIKit

@MainActor
enum ProgressHUD {
    static func configure() {
        SVProgressHUD.setMinimumDismissTimeInterval(0.5)
        SVProgressHUD.setGraceTimeInterval(0)
        SVProgressHUD.setDefaultMaskType(SVProgressHUDMaskType.clear)
        SVProgressHUD.setDefaultStyle(SVProgressHUDStyle.light)
        SVProgressHUD.setHapticsEnabled(true)
    }

    static func updateStyle(isDark: Bool) {
        SVProgressHUD.setDefaultStyle(isDark ? .dark : .light)
    }

    static func setHapticsEnabled(_ isEnabled: Bool) {
        SVProgressHUD.setHapticsEnabled(isEnabled)
    }

    static func show(status: String? = nil) {
        SVProgressHUD.show(withStatus: status)
    }

    static func showError(status: String? = nil) {
        SVProgressHUD.showError(withStatus: status)
    }

    static func showSuccess(status: String? = nil) {
        SVProgressHUD.showSuccess(withStatus: status)
    }

    static func dismiss() {
        SVProgressHUD.dismiss()
    }

    static func dismissWithDelay(_ delay: TimeInterval = 1.5, completion: (() -> Void)? = nil) {
        SVProgressHUD.dismiss(withDelay: delay, completion: completion)
    }
}
