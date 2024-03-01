import shared
import SwiftUI
import UIKit

final class AppRouter {
    weak var viewController: UIViewController?

    func route(_ route: Route) {
        guard let viewController else {
            return assertionFailure("AppRouter: viewController is nil")
        }

        let viewControllerToPresent: UIViewController = {
            switch route {
            case .auth(let isInSignUpMode, let moduleOutput):
                let assembly = AuthSocialAssembly(
                    isInSignUpMode: isInSignUpMode,
                    output: moduleOutput
                )
                return UIHostingController(rootView: assembly.makeModule())
            case .studyPlan(let appTabBarControllerDelegate):
                return AppTabBarController(
                    initialTab: .studyPlan,
                    availableTabs: AppTabItemsAvailabilityService.shared.getAvailableTabs(),
                    appTabBarControllerDelegate: appTabBarControllerDelegate
                )
            case .studyPlanWithStep(let appTabBarControllerDelegate, let stepRoute):
                let tabBarController = AppTabBarController(
                    initialTab: .studyPlan,
                    availableTabs: AppTabItemsAvailabilityService.shared.getAvailableTabs(),
                    appTabBarControllerDelegate: appTabBarControllerDelegate
                )

                if !tabBarController.isViewLoaded {
                    _ = tabBarController.view
                }

                DispatchQueue.main.async {
                    let index = tabBarController.selectedIndex

                    guard
                        let navigationController = tabBarController.children[index] as? UINavigationController
                    else {
                        return assertionFailure("AppRouter: Expected UINavigationController")
                    }

                    let stepAssembly = StepAssembly(stepRoute: stepRoute)
                    navigationController.pushViewController(stepAssembly.makeModule(), animated: false)
                }

                return tabBarController
            case .trackSelection:
                let assembly = TrackSelectionListAssembly(isNewUserMode: true)
                let navigationController = UINavigationController(
                    rootViewController: assembly.makeModule()
                )
                navigationController.navigationBar.prefersLargeTitles = true
                return navigationController
            case .onboarding(let moduleOutput):
                let assembly = WelcomeAssembly(output: moduleOutput)
                return UIHostingController(rootView: assembly.makeModule())
            case .firstProblemOnboarding(let isNewUserMode, let moduleOutput):
                let assembly = FirstProblemOnboardingAssembly(
                    isNewUserMode: isNewUserMode,
                    output: moduleOutput
                )
                return assembly.makeModule()
            case .notificationOnboarding(let moduleOutput):
                let assembly = NotificationsOnboardingAssembly(output: moduleOutput)
                return assembly.makeModule()
            case .usersQuestionnaireOnboarding(let moduleOutput):
                let assembly = UsersQuestionnaireOnboardingAssembly(moduleOutput: moduleOutput)
                return assembly.makeModule()
            }
        }()

        let fromViewController = viewController.children.first { childrenViewController in
            if childrenViewController is UIHostingController<PlaceholderView> {
                return false
            }
            return true
        }
        if let fromViewController,
           type(of: fromViewController) == type(of: viewControllerToPresent) {
            return
        }

        assert(viewController.children.count <= 2)

        swapRootViewController(
            for: viewController,
            from: fromViewController,
            to: viewControllerToPresent
        )
    }

    // MARK: Private API

    private func swapRootViewController(
        for viewController: UIViewController,
        from oldViewController: UIViewController?,
        to newViewController: UIViewController
    ) {
        oldViewController?.willMove(toParent: nil)

        viewController.addChild(newViewController)

        viewController.view.addSubview(newViewController.view)
        newViewController.view.translatesAutoresizingMaskIntoConstraints = false
        newViewController.view.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        newViewController.view.alpha = 0
        newViewController.view.setNeedsLayout()
        newViewController.view.layoutIfNeeded()

        UIView.animate(
            withDuration: Animation.swapRootViewControllerAnimationDuration,
            delay: 0,
            options: .transitionFlipFromLeft,
            animations: {
                newViewController.view.alpha = 1
                oldViewController?.view.alpha = 0
            },
            completion: { isFinished in
                guard isFinished else {
                    return
                }

                oldViewController?.view.removeFromSuperview()
                oldViewController?.removeFromParent()

                newViewController.didMove(toParent: viewController)
            }
        )
    }

    // MARK: Inner Types

    enum Route {
        case auth(isInSignUpMode: Bool, moduleOutput: AuthOutputProtocol?)
        case studyPlan(appTabBarControllerDelegate: AppTabBarControllerDelegate?)
        case studyPlanWithStep(appTabBarControllerDelegate: AppTabBarControllerDelegate?, stepRoute: StepRoute)
        case trackSelection
        case onboarding(moduleOutput: WelcomeOutputProtocol?)
        case firstProblemOnboarding(isNewUserMode: Bool, moduleOutput: FirstProblemOnboardingOutputProtocol?)
        case notificationOnboarding(moduleOutput: NotificationsOnboardingOutputProtocol?)
        case usersQuestionnaireOnboarding(moduleOutput: UsersQuestionnaireOnboardingOutputProtocol?)
    }

    enum Animation {
        static let swapRootViewControllerAnimationDuration: TimeInterval = 0.3
    }
}
