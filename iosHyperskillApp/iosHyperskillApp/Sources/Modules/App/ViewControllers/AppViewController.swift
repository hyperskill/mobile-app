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

        viewModel.startListening()
        viewModel.loadApp()

        updateProgressHUDStyle()
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
        let viewControllerToPresent: UIViewController? = {
            guard case .navigateTo(let navigateToViewAction) = viewAction else {
                return nil
            }

            switch AppFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .onboardingScreen:
                return UIHostingController(rootView: OnboardingAssembly(output: viewModel).makeModule())
            case .homeScreen:
                let controller = AppTabBarController()
                controller.appTabBarControllerDelegate = viewModel
                return controller
            case .authScreen(let data):
                let assembly = AuthSocialAssembly(isInSignUpMode: data.isInSignUpMode, output: viewModel)
                return UIHostingController(rootView: assembly.makeModule())
            case .newUserScreen:
                return UIHostingController(rootView: AuthNewUserPlaceholderAssembly(output: viewModel).makeModule())
            }
        }()

        guard let viewControllerToPresent else {
            return
        }

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
        viewModel.loadApp(forceUpdate: true)
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
