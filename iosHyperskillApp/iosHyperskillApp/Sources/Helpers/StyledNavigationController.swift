import SnapKit
import UIKit

final class StyledNavigationController: UINavigationController {
    private lazy var progressView = StepToolbarProgressView()

    override func viewDidLoad() {
        super.viewDidLoad()
        setupProgressView()
    }
}

// MARK: - UIViewController (StepToolbarProgressView) -

extension StyledNavigationController {
    func setProgress(_ progress: Float, animated: Bool) {
        Thread.dispatchOnMainThread {
            self.progressView.setHidden(false, animated: animated) {
                self.progressView.setProgress(progress, animated: animated)
            }
        }
    }

    private func setupProgressView() {
        progressView.setHidden(true, animated: false, completion: nil)

        navigationBar.addSubview(progressView)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.snp.makeConstraints { make in
            make.leading.trailing.equalToSuperview()
            make.bottom.equalTo(navigationBar.snp.bottom)
        }
    }
}

// MARK: - UIViewController (StyledNavigationController) -

extension UIViewController {
    var styledNavigationController: StyledNavigationController? {
        navigationController as? StyledNavigationController
    }
}
