import shared
import SnapKit
import SwiftUI
import UIKit

protocol AppViewControllerProtocol: AnyObject {
    func displayState(_ state: AppFeatureState)
    func displayViewAction(_ viewAction: AppFeatureActionViewAction)
}

final class AppViewController: UIViewController {
    private let viewModel: AppViewModel

    var appView: AppUIKitView? { view as? AppUIKitView }

    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        let view = AppUIKitView(frame: UIScreen.main.bounds, delegate: self)
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
    func displayState(_ state: AppFeatureState) {
        appView?.renderState(state)
    }

    func displayViewAction(_ viewAction: AppFeatureActionViewAction) {
        print("AppViewController :: \(#function), viewAction = \(viewAction)")

        let viewControllerToPresent: UIViewController? = {
            switch viewAction {
            case is AppFeatureActionViewActionNavigateToOnboardingScreen:
                return UIHostingController(rootView: OnboardingAssembly(output: viewModel).makeModule())
            case is AppFeatureActionViewActionNavigateToHomeScreen:
                return nil
            case is AppFeatureActionViewActionNavigateToAuthScreen:
                return UIHostingController(rootView: AuthSocialAssembly(output: viewModel).makeModule())
            case is AppFeatureActionViewActionNavigateToNewUserScreen:
                return UIHostingController(rootView: AuthNewUserPlaceholderAssembly(output: viewModel).makeModule())
            default:
                print("AppView :: unhandled viewAction = \(viewAction)")
                return nil
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
            withDuration: 0.3,
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
    func appViewPlaceholderActionButtonTapped(_ view: AppUIKitView) {
        viewModel.loadApp(forceUpdate: true)
    }

    func appView(
        _ view: AppUIKitView,
        didRequestAddPlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    ) {
        addChild(hostingController)
    }

    func appView(
        _ view: AppUIKitView,
        didConfigurePlaceholderHostingController hostingController: UIHostingController<PlaceholderView>
    ) {
        hostingController.didMove(toParent: self)
    }
}
