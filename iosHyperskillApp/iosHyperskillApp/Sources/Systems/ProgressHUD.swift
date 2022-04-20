import SVProgressHUD
import UIKit

enum ProgressHUD {
    static func configure() {
        withSafe {
            SVProgressHUD.setMinimumDismissTimeInterval(0.5)
            SVProgressHUD.setDefaultMaskType(SVProgressHUDMaskType.clear)
            SVProgressHUD.setDefaultStyle(SVProgressHUDStyle.light)
            SVProgressHUD.setHapticsEnabled(true)
        }
    }

    static func updateStyle(isDark: Bool) {
        withSafe {
            SVProgressHUD.setDefaultStyle(isDark ? .dark : .light)
        }
    }

    static func show(status: String? = nil) {
        withSafe {
            SVProgressHUD.show(withStatus: status)
        }
    }

    static func showError(status: String? = nil) {
        withSafe {
            SVProgressHUD.showError(withStatus: status)
        }
    }

    static func showSuccess(status: String? = nil) {
        withSafe {
            SVProgressHUD.showSuccess(withStatus: status)
        }
    }

    static func dismiss() {
        withSafe {
            SVProgressHUD.dismiss()
        }
    }

    private static func withSafe(_ block: @escaping () -> Void) {
        guard UIApplication.shared.delegate?.window?.flatMap({ $0 }) != nil else {
            return
        }

        DispatchQueue.main.async {
            block()
        }
    }
}
