import SwiftUI

enum IntrospectTestUtils {
    enum Constants {
        static let timeout: TimeInterval = 3
    }

    static func present<ViewType: View>(view: ViewType) {

        let hostingController = UIHostingController(rootView: view)

        let application = UIApplication.shared
        application.windows.forEach { window in
            if let presentedViewController = window.rootViewController?.presentedViewController {
                presentedViewController.dismiss(animated: false, completion: nil)
            }
            window.isHidden = true
        }

        let window = UIWindow(frame: UIScreen.main.bounds)
        window.layer.speed = 10
        
        hostingController.beginAppearanceTransition(true, animated: false)
        window.rootViewController = hostingController
        window.makeKeyAndVisible()
        window.layoutIfNeeded()
        hostingController.endAppearanceTransition()
    }
}
