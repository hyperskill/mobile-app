import UIKit

final class WebViewNavigationController: UINavigationController {
    override func viewDidLoad() {
        super.viewDidLoad()

        // ALTAPPS-453: Fix iOS 16 Password AutoFill crash
        if #available(iOS 16.0, *) {
            InterfaceOrientationChangesPublisher.publishSupportedInterfaceOrientationsDidChange(to: .all)
        }
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)

        if #available(iOS 16.0, *) {
            InterfaceOrientationChangesPublisher.publishSupportedInterfaceOrientationsResetToDefault()
        }
    }
}
