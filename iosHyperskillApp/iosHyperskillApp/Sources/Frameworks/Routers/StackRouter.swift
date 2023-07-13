import SwiftUI
import UIKit

protocol StackRouterProtocol: AnyObject {
    func pushViewController(_ viewController: UIViewController, animated: Bool)
    func pushViewController(_ viewController: UIViewController, animated: Bool, completion: @escaping (() -> Void))

    func popViewController(animated: Bool)
    func popViewController(animated: Bool, completion: @escaping (() -> Void))

    func popToRootViewController(animated: Bool)
    func popToRootViewController(animated: Bool, completion: @escaping (() -> Void))

    func replaceTopViewController(_ newTopViewController: UIViewController, animated: Bool)
}

extension StackRouterProtocol {
    func pushViewController(_ viewController: UIViewController) {
        pushViewController(viewController, animated: true)
    }

    func pushViewController(_ viewController: UIViewController, completion: @escaping (() -> Void)) {
        pushViewController(viewController, animated: true, completion: completion)
    }

    func popViewController() {
        popViewController(animated: true)
    }

    func popViewController(completion: @escaping (() -> Void)) {
        popViewController(animated: true, completion: completion)
    }

    func popToRootViewController() {
        popToRootViewController(animated: true)
    }

    func popToRootViewController(completion: @escaping (() -> Void)) {
        popToRootViewController(animated: true, completion: completion)
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

    func pushViewController(_ viewController: UIViewController, animated: Bool, completion: @escaping (() -> Void)) {
        navigationController?.pushViewController(viewController, animated: animated, completion: completion)
    }

    func popViewController(animated: Bool) {
        navigationController?.popViewController(animated: animated)
    }

    func popViewController(animated: Bool, completion: @escaping (() -> Void)) {
        navigationController?.popViewController(animated: animated, completion: completion)
    }

    func popToRootViewController(animated: Bool) {
        navigationController?.popToRootViewController(animated: animated)
    }

    func popToRootViewController(animated: Bool, completion: @escaping (() -> Void)) {
        navigationController?.popToRootViewController(animated: animated, completion: completion)
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

    func replaceTopView(_ view: some View, animated: Bool = true) {
        let hostingController = UIHostingController(rootView: view)
        replaceTopViewController(hostingController, animated: animated)
    }
}
