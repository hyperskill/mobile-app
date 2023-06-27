import shared
import SnapKit
import SwiftUI
import UIKit

protocol AppViewControllerProtocol: AnyObject {
    func displayState(_ state: AppFeatureStateKs)
    func displayViewAction(_ viewAction: AppFeatureActionViewActionKs)
}

extension AppViewController {
    enum Animation {
        static let swapRootViewControllerAnimationDuration: TimeInterval = 0.3

        fileprivate static let clickedNotificationViewActionNavigateToHomeCompletionAnimationDelay: TimeInterval = 0.33
        fileprivate static let clickedNotificationViewActionDismissProgressHUDDelay: TimeInterval = 0.33
    }
}

final class AppViewController: UIViewController {
    private let viewModel: AppViewModel

    var appView: AppView? { view as? AppView }

    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        let view = AppView(frame: UIScreen.main.bounds, delegate: self)
        self.view = view
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        updateProgressHUDStyle()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel.startListening()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewModel.stopListening()
    }

    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)

        view.performBlockIfAppearanceChanged(from: previousTraitCollection) {
            updateProgressHUDStyle()
        }
    }

    // MARK: Private API

    private func updateProgressHUDStyle() {
        ProgressHUD.updateStyle(isDark: view.isDarkInterfaceStyle)
    }
}

// MARK: - AppViewController: AppViewControllerProtocol -

extension AppViewController: AppViewControllerProtocol {
    func displayState(_ state: AppFeatureStateKs) {
        appView?.renderState(state)
    }

    func displayViewAction(_ viewAction: AppFeatureActionViewActionKs) {
        switch viewAction {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                AppFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        case .streakRecoveryViewAction(let streakRecoveryViewAction):
            handleStreakRecoveryViewAction(
                StreakRecoveryFeatureActionViewActionKs(streakRecoveryViewAction.viewAction)
            )
        case .clickedNotificationViewAction(let clickedNotificationViewAction):
            handleClickedNotificationViewAction(
                NotificationClickHandlingFeatureActionViewActionKs(clickedNotificationViewAction.viewAction)
            )
        }
    }

    private func handleNavigateToViewAction(_ viewAction: AppFeatureActionViewActionNavigateToKs) {
        let viewControllerToPresent: UIViewController = {
            switch viewAction {
            case .onboardingScreen:
                return UIHostingController(rootView: OnboardingAssembly(output: viewModel).makeModule())
            case .homeScreen:
                let controller = AppTabBarController()
                controller.appTabBarControllerDelegate = viewModel
                return controller
            case .authScreen(let data):
                let assembly = AuthSocialAssembly(isInSignUpMode: data.isInSignUpMode, output: viewModel)
                return UIHostingController(rootView: assembly.makeModule())
            case .trackSelectionScreen:
                let assembly = TrackSelectionListAssembly(isNewUserMode: true)
                let navigationController = UINavigationController(
                    rootViewController: assembly.makeModule()
                )
                navigationController.navigationBar.prefersLargeTitles = true
                return navigationController
            }
        }()

        let fromViewController = children.first { viewController in
            if viewController is UIHostingController<PlaceholderView> {
                return false
            }
            return true
        }
        if let fromViewController,
           type(of: fromViewController) == type(of: viewControllerToPresent) {
            return
        }

        assert(children.count <= 2)

        swapRootViewController(from: fromViewController, to: viewControllerToPresent)
    }

    private func handleStreakRecoveryViewAction(_ viewAction: StreakRecoveryFeatureActionViewActionKs) {
        switch viewAction {
        case .showRecoveryStreakModal(let showRecoveryStreakModal):
            let modalViewController = StreakRecoveryModalViewController(
                recoveryPriceAmount: showRecoveryStreakModal.recoveryPriceAmountLabel,
                recoveryPriceLabel: showRecoveryStreakModal.recoveryPriceGemsLabel,
                modalText: showRecoveryStreakModal.modalText,
                delegate: viewModel
            )
            presentIfPanModalWithCustomModalPresentationStyle(modalViewController)
        case .hideStreakRecoveryModal:
            dismiss(animated: true)
        case .showNetworkRequestStatus(let showNetworkRequestStatus):
            switch StreakRecoveryFeatureActionViewActionShowNetworkRequestStatusKs(showNetworkRequestStatus) {
            case .error(let showErrorStatusViewAction):
                ProgressHUD.showError(status: showErrorStatusViewAction.message)
            case .loading:
                ProgressHUD.show()
            case .success(let showSuccessStatusViewAction):
                ProgressHUD.showSuccess(status: showSuccessStatusViewAction.message)
            }
        }
    }

    private func handleClickedNotificationViewAction(
        _ viewAction: NotificationClickHandlingFeatureActionViewActionKs
    ) {
        switch viewAction {
        case .navigateTo(let navigateToViewAction):
            handleClickedNotificationNavigateToViewAction(
                NotificationClickHandlingFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        case .setLoadingShowed(let setLoadingShowedViewAction):
            if setLoadingShowedViewAction.isLoadingShowed {
                ProgressHUD.show()
            } else {
                ProgressHUD.dismissWithDelay(Animation.clickedNotificationViewActionDismissProgressHUDDelay)
            }
        }
    }

    private func handleClickedNotificationNavigateToViewAction(
        _ viewAction: NotificationClickHandlingFeatureActionViewActionNavigateToKs
    ) {
        func route() {
            switch viewAction {
            case .home:
                TabBarRouter(tab: .home).route()
            case .profile:
                TabBarRouter(tab: .profile).route()
            case .stepScreen(let navigateToStepScreenViewAction):
                navigateToHomeAndPresent {
                    let assembly = StepAssembly(stepRoute: navigateToStepScreenViewAction.stepRoute)

                    let sourcelessRouter = SourcelessRouter()
                    sourcelessRouter.currentNavigation?.pushViewController(assembly.makeModule(), animated: true)
                }
            case .studyPlan:
                TabBarRouter(tab: .studyPlan).route()
            case .topicRepetition:
                navigateToHomeAndPresent {
                    let assembly = TopicsRepetitionsAssembly()

                    let sourcelessRouter = SourcelessRouter()
                    sourcelessRouter.currentNavigation?.pushViewController(assembly.makeModule(), animated: true)
                }
            }
        }

        func navigateToHomeAndPresent(_ navigateToHomeCompletionHandler: @escaping () -> Void) {
            TabBarRouter(tab: .home).route()
            DispatchQueue.main.asyncAfter(
                deadline: .now() + Animation.clickedNotificationViewActionNavigateToHomeCompletionAnimationDelay,
                execute: navigateToHomeCompletionHandler
            )
        }

        // Display home screen if needed
        handleNavigateToViewAction(.homeScreen)

        DispatchQueue.main.async {
            route()
        }
    }

    private func swapRootViewController(
        from oldViewController: UIViewController?,
        to newViewController: UIViewController
    ) {
        oldViewController?.willMove(toParent: nil)

        addChild(newViewController)

        view.addSubview(newViewController.view)
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

                newViewController.didMove(toParent: self)
            }
        )
    }
}

// MARK: - AppViewController: AppViewDelegate -

extension AppViewController: AppViewDelegate {
    func appViewPlaceholderActionButtonTapped(_ view: AppView) {
        viewModel.doRetryLoadApp()
    }

    func appView(
        _ view: AppView,
        didRequestAddPlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    ) {
        addChild(hostingController)
    }

    func appView(
        _ view: AppView,
        didConfigurePlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    ) {
        hostingController.didMove(toParent: self)
    }
}
