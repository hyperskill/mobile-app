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
            case .studyPlanWithPaywall(let appTabBarControllerDelegate, let paywallTransitionSource):
                let tabBarController = AppTabBarController(
                    initialTab: .studyPlan,
                    availableTabs: AppTabItemsAvailabilityService.shared.getAvailableTabs(),
                    appTabBarControllerDelegate: appTabBarControllerDelegate
                )

                if !tabBarController.isViewLoaded {
                    _ = tabBarController.view
                }

                DispatchQueue.main.async {
                    let currentRootViewController = tabBarController.children[tabBarController.selectedIndex]
                    currentRootViewController.present(
                        PaywallAssembly(source: paywallTransitionSource).makeModule(),
                        animated: false
                    )
                }

                return tabBarController
            case .trackSelection:
                let assembly = TrackSelectionListAssembly(isNewUserMode: true)
                let navigationController = UINavigationController(
                    rootViewController: assembly.makeModule()
                )
                navigationController.navigationBar.prefersLargeTitles = true
                return navigationController
            case .firstProblemOnboarding(let isNewUserMode, let moduleOutput):
                let assembly = FirstProblemOnboardingAssembly(
                    isNewUserMode: isNewUserMode,
                    output: moduleOutput
                )
                return assembly.makeModule()
            case .welcome(let moduleOutput):
                let assembly = WelcomeAssembly(output: moduleOutput)
                return UIHostingController(rootView: assembly.makeModule())
            case .welcomeOnboarding(let params, let moduleOutput):
                let assembly = WelcomeOnboardingAssembly(
                    params: params,
                    moduleOutput: moduleOutput
                )
                return assembly.makeModule()
            case .paywall(let paywallTransitionSource), .paywallModal(let paywallTransitionSource):
                let assembly = PaywallAssembly(source: paywallTransitionSource)
                return assembly.makeModule()
            }
        }()

        switch route {
        case .paywallModal:
            let isAlreadyPresented = (
                SourcelessRouter().currentPresentedViewController() as? PaywallHostingController
            ) != nil

            if !isAlreadyPresented {
                viewController.present(viewControllerToPresent, animated: true)
            }
        default:
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

            UIViewController.swapRootViewController(
                for: viewController,
                from: fromViewController,
                to: viewControllerToPresent
            )
        }
    }

    // MARK: Inner Types

    enum Route {
        case auth(isInSignUpMode: Bool, moduleOutput: AuthOutputProtocol?)
        case studyPlan(appTabBarControllerDelegate: AppTabBarControllerDelegate?)
        case studyPlanWithStep(appTabBarControllerDelegate: AppTabBarControllerDelegate?, stepRoute: StepRoute)
        case studyPlanWithPaywall(
            appTabBarControllerDelegate: AppTabBarControllerDelegate?,
            paywallTransitionSource: PaywallTransitionSource
        )
        case trackSelection
        case firstProblemOnboarding(isNewUserMode: Bool, moduleOutput: FirstProblemOnboardingOutputProtocol?)
        case welcome(moduleOutput: WelcomeOutputProtocol?)
        case welcomeOnboarding(params: WelcomeOnboardingFeatureParams, moduleOutput: WelcomeOnboardingOutputProtocol?)
        case paywall(paywallTransitionSource: PaywallTransitionSource)
        case paywallModal(paywallTransitionSource: PaywallTransitionSource)
    }

    enum Animation {
        static let swapRootViewControllerAnimationDuration: TimeInterval = 0.3
    }
}
