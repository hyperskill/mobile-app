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
}

// MARK: - UIViewController (StepToolbarProgressView) -

extension StyledNavigationController {
    func setProgress(_ progress: Float, animated: Bool) {
        progressView.setHidden(false, animated: animated) { [weak progressView] in
            progressView?.setProgress(progress, animated: animated)
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
            make.bottom.equalTo(navigationBar.snp.bottom)
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
