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
        fileprivate static let clickedNotificationViewActionNavigateToHomeCompletionDelay: TimeInterval = 0.33
        fileprivate static let clickedNotificationViewActionDismissProgressHUDDelay: TimeInterval = 0.33
    }
}

final class AppViewController: UIViewController {
    private let viewModel: AppViewModel
    private let router: AppRouter

    var appView: AppView? { view as? AppView }

    init(viewModel: AppViewModel, router: AppRouter) {
        self.viewModel = viewModel
        self.router = router
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
        viewModel.doLoadApp()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel.startListening()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewModel.stopListening()
    }
}

// MARK: - AppViewController: AppViewControllerProtocol -

extension AppViewController: AppViewControllerProtocol {
    func displayState(_ state: AppFeatureStateKs) {
        if case .ready(let data) = state {
            AppTabItemsAvailabilityService.shared.setIsMobileLeaderboardsEnabled(data.isMobileLeaderboardsEnabled)
        }

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
        switch viewAction {
        case .authScreen(let data):
            router.route(.auth(isInSignUpMode: data.isInSignUpMode, moduleOutput: viewModel))
        case .welcomeScreen:
            router.route(.welcome(moduleOutput: viewModel))
        case .studyPlan:
            router.route(.studyPlan(appTabBarControllerDelegate: viewModel))
        case .trackSelectionScreen:
            router.route(.trackSelection)
        case .firstProblemOnboarding(let data):
            router.route(.firstProblemOnboarding(isNewUserMode: data.isNewUserMode, moduleOutput: viewModel))
        case .paywall(let data):
            router.route(.paywallModal(paywallTransitionSource: data.paywallTransitionSource))
        case .studyPlanWithPaywall(let data):
            router.route(
                .studyPlanWithPaywall(
                    appTabBarControllerDelegate: viewModel,
                    paywallTransitionSource: data.paywallTransitionSource
                )
            )
        case .studyPlanWithStep(let data):
            router.route(.studyPlanWithStep(appTabBarControllerDelegate: viewModel, stepRoute: data.stepRoute))
        case .welcomeOnboarding(let data):
            Task(priority: .userInitiated) {
                let currentAuthorizationStatus = await NotificationPermissionStatus.current

                await MainActor.run {
                    let params = WelcomeOnboardingFeatureParams(
                        profile: data.profile,
                        isNotificationPermissionGranted: currentAuthorizationStatus.isRegistered
                    )
                    router.route(.welcomeOnboarding(params: params, moduleOutput: viewModel))
                }
            }
        }
    }

    private func handleStreakRecoveryViewAction(_ viewAction: StreakRecoveryFeatureActionViewActionKs) {
        switch viewAction {
        case .showRecoveryStreakModal(let showRecoveryStreakModal):
            let modalViewController = StreakRecoveryModalViewController(
                recoveryPriceAmount: showRecoveryStreakModal.recoveryPriceAmountLabel,
                recoveryPriceLabel: showRecoveryStreakModal.recoveryPriceGemsLabel,
                modalText: showRecoveryStreakModal.modalText,
                isFirstTimeOffer: showRecoveryStreakModal.isFirstTimeOffer,
                nextRecoveryPriceText: showRecoveryStreakModal.nextRecoveryPriceText,
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
        case .showEarnedBadgeModal(let showEarnedBadgeModalViewAction):
            let earnedBadgeModalAssembly = BadgeEarnedModalAssembly(
                badge: showEarnedBadgeModalViewAction.badge,
                delegate: viewModel
            )
            presentIfPanModalWithCustomModalPresentationStyle(earnedBadgeModalAssembly.makeModule())
        }
    }

    private func handleClickedNotificationNavigateToViewAction(
        _ viewAction: NotificationClickHandlingFeatureActionViewActionNavigateToKs
    ) {
        func route() {
            switch viewAction {
            case .profile:
                TabBarRouter(tab: .profile).route()
            case .stepScreen(let navigateToStepScreenViewAction):
                navigate(to: .studyPlan) {
                    let assembly = StepAssembly(stepRoute: navigateToStepScreenViewAction.stepRoute)

                    let sourcelessRouter = SourcelessRouter()
                    sourcelessRouter.currentNavigation?.pushViewController(assembly.makeModule(), animated: true)
                }
            case .studyPlan:
                TabBarRouter(tab: .studyPlan).route()
            case .topicRepetition:
                navigate(to: .home) {
                    let assembly = TopicsRepetitionsAssembly()

                    let sourcelessRouter = SourcelessRouter()
                    sourcelessRouter.currentNavigation?.pushViewController(assembly.makeModule(), animated: true)
                }
            }
        }

        func navigate(
            to targetTab: AppTabItem,
            andPerformTargetNavigation targetNavigationBlock: @escaping () -> Void
        ) {
            TabBarRouter(tab: targetTab).route()
            DispatchQueue.main.asyncAfter(
                deadline: .now() + Animation.clickedNotificationViewActionNavigateToHomeCompletionDelay,
                execute: targetNavigationBlock
            )
        }

        // Add AppTabBarController into view hierarchy if needed
        handleNavigateToViewAction(.studyPlan)

        DispatchQueue.main.async {
            route()
        }
    }
}

// MARK: - AppViewController: AppViewDelegate -

extension AppViewController: AppViewDelegate {
    func appViewPlaceholderActionButtonTapped(_ view: AppView) {
        viewModel.doLoadApp(forceUpdate: true)
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
