import SwiftUI
import UIKit

protocol StackRouterProtocol: AnyObject {
    func pushViewController(_ viewController: UIViewController, animated: Bool)
    func popViewController(animated: Bool)
}

extension StackRouterProtocol {
    func pushViewController(_ viewController: UIViewController) {
        pushViewController(viewController, animated: true)
    }

    func popViewController(animated: Bool) {
        popViewController(animated: false)
    }
}

class StackRouter: StackRouterProtocol {
    weak var rootViewController: UIViewController?

    init(rootViewController: UIViewController? = nil) {
        self.rootViewController = rootViewController
    }

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

    func pushViewController(_ viewController: UIViewController, animated: Bool) {
        navigationController?.pushViewController(viewController, animated: animated)
    }

    func popViewController(animated: Bool) {
        navigationController?.popViewController(animated: animated)
    }
}

class SwiftUIStackRouter: StackRouter, ObservableObject {
    func pushView(_ view: some View, animated: Bool = true) {
        let hostingController = UIHostingController(rootView: view)
        pushViewController(hostingController, animated: animated)
    }

    func popView(animated: Bool = false) {
        popViewController(animated: animated)
    }
}
