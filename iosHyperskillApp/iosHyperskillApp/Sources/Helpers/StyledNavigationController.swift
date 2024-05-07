import SnapKit
import SwiftUI
import UIKit

final class StyledNavigationController: UINavigationController {
    private lazy var progressView = StepToolbarProgressView()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupProgressView()
    }

    override func popViewController(animated: Bool) -> UIViewController? {
        handleProgressViewVisibilityOnPopViewController(isPopToRoot: false, animated: animated)
        return super.popViewController(animated: animated)
    }

    override func popToRootViewController(animated: Bool) -> [UIViewController]? {
        handleProgressViewVisibilityOnPopViewController(isPopToRoot: true, animated: animated)
        return super.popToRootViewController(animated: animated)
    }

    override func viewWillTransition(to size: CGSize, with coordinator: any UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        coordinator.animate(alongsideTransition: nil) { [weak self] _ in
            self?.progressView.handleOrientationChange()
        }
    }
}

// MARK: - UIViewController (StepToolbarProgressView) -

extension StyledNavigationController {
    func setProgress(_ progress: Float, animated: Bool) {
        let playWowAnimation = !progressView.isHidden
            && progressView.progress != nil
            && progressView.progress.require() < progress
        progressView.setHidden(false, animated: animated) { [weak progressView] in
            progressView?.setProgress(progress, animated: animated, playWowAnimation: playWowAnimation)
        }
    }

    func hideProgress() {
        progressView.setHidden(true, animated: true)
    }

    private func setupProgressView() {
        progressView.setHidden(true, animated: false)

        navigationBar.addSubview(progressView)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.snp.makeConstraints { make in
            make.leading.trailing.equalToSuperview()
            make.bottom.equalTo(navigationBar.snp.bottom).offset(progressView.appearance.progressBarHeight)
        }
    }

    private func handleProgressViewVisibilityOnPopViewController(isPopToRoot: Bool, animated: Bool) {
        let isActuallyPopToRoot = isPopToRoot || viewControllers.count == 2

        if isActuallyPopToRoot || !isAncestorAStepView() {
            progressView.setHidden(true, animated: animated)
        }
    }

    private func isAncestorAStepView() -> Bool {
        if let ancestor = viewControllers[safe: viewControllers.count - 2] {
            return ancestor is UIHostingController<StepView>
        }
        return false
    }
}

// MARK: - UIViewController (StyledNavigationController) -

extension UIViewController {
    var styledNavigationController: StyledNavigationController? {
        navigationController as? StyledNavigationController
    }
}
