import SwiftUI
import UIKit

protocol PushRouterProtocol: AnyObject {
    func pushViewController(_ viewController: UIViewController, animated: Bool)
}

extension PushRouterProtocol {
    func pushViewController(_ viewController: UIViewController) {
        pushViewController(viewController, animated: true)
    }
}

class PushRouter: PushRouterProtocol {
    weak var rootViewController: UIViewController?

    init(rootViewController: UIViewController? = nil) {
        self.rootViewController = rootViewController
    }

    func pushViewController(_ viewController: UIViewController, animated: Bool) {
        let navigationController: UINavigationController? =
          rootViewController?.navigationController ?? rootViewController as? UINavigationController

        guard let navigationController else {
            return assertionFailure(
                "Router :: no navigationController in rootViewController = \(String(describing: rootViewController)) hierarchy"
            )
        }

        navigationController.pushViewController(viewController, animated: animated)
    }
}

class SwiftUIPushRouter: PushRouter, ObservableObject {
    func pushView(_ view: some View, animated: Bool = true) {
        let hostingController = UIHostingController(rootView: view)
        pushViewController(hostingController, animated: animated)
    }
}
