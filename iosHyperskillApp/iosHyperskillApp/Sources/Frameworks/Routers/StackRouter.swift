import SwiftUI
import UIKit

protocol StackRouterProtocol: AnyObject {
    func pushViewController(_ viewController: UIViewController, animated: Bool)
    func popViewController(animated: Bool)
    func replaceTopViewController(_ newTopViewController: UIViewController, animated: Bool)
}

extension StackRouterProtocol {
    func pushViewController(_ viewController: UIViewController) {
        pushViewController(viewController, animated: true)
    }

    func popViewController() {
        popViewController(animated: true)
    }
}

class StackRouter: StackRouterProtocol {
    weak var rootViewController: UIViewController?

    private var navigationController: UINavigationController? {
        let navigationController: UINavigationController? =
          rootViewController?.navigationController ?? rootViewController as? UINavigationController

        guard let navigationController else {
            assertionFailure(
                "Router :: no navigationController in rootViewController = \(String(describing: rootViewController)) hierarchy"
            )
            return nil
        }

        return navigationController
    }

    init(rootViewController: UIViewController? = nil) {
        self.rootViewController = rootViewController
    }

    func pushViewController(_ viewController: UIViewController, animated: Bool) {
        navigationController?.pushViewController(viewController, animated: animated)
    }

    func popViewController(animated: Bool) {
        navigationController?.popViewController(animated: animated)
    }

    func replaceTopViewController(_ newTopViewController: UIViewController, animated: Bool) {
        guard let navigationController,
              !navigationController.viewControllers.isEmpty else {
            return
        }

        var viewControllers = navigationController.viewControllers
        viewControllers[viewControllers.count - 1] = newTopViewController

        navigationController.setViewControllers(viewControllers, animated: animated)
    }
}

class SwiftUIStackRouter: StackRouter, ObservableObject {
    func pushView(_ view: some View, animated: Bool = true) {
        let hostingController = UIHostingController(rootView: view)
        pushViewController(hostingController, animated: animated)
    }
}
