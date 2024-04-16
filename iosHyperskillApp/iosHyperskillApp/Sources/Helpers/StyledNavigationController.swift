import SnapKit
import UIKit

final class StyledNavigationController: UINavigationController {
    enum Appearance {
        static let progressBarHeight: CGFloat = 2
    }

    private lazy var progressView: UIProgressView = {
        let progressView = UIProgressView(progressViewStyle: .bar)
        progressView.trackTintColor = ColorPalette.onSurfaceAlpha9
        progressView.progressTintColor = ColorPalette.primary
        return progressView
    }()

    func setProgress(_ progress: Float, animated: Bool) {
        setupProgressViewIfNeeded()
        progressView.setProgress(progress, animated: animated)
    }

    private func setupProgressViewIfNeeded() {
        guard progressView.superview == nil else {
            return
        }

        navigationBar.addSubview(progressView)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.snp.makeConstraints { make in
            make.leading.trailing.equalToSuperview()
            make.bottom.equalTo(navigationBar.snp.bottom)
            make.height.equalTo(Appearance.progressBarHeight)
        }
    }
}

// MARK: - UIViewController (StyledNavigationController) -

extension UIViewController {
    var styledNavigationController: StyledNavigationController? {
        navigationController as? StyledNavigationController
    }
}
