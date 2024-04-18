import SnapKit
import UIKit

extension StepToolbarProgressView {
    struct Appearance {
        let trackTintColor = ColorPalette.onSurfaceAlpha9
        let progressTintColor = ColorPalette.primary

        let height: CGFloat = 2
    }

    enum Animation {
        static let visibilityDuration: TimeInterval = 0.3
    }
}

final class StepToolbarProgressView: UIView {
    let appearance: Appearance

    private lazy var progressView: UIProgressView = {
        let progressView = UIProgressView(progressViewStyle: .bar)
        progressView.trackTintColor = appearance.trackTintColor
        progressView.progressTintColor = appearance.progressTintColor
        return progressView
    }()

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func setProgress(_ progress: Float, animated: Bool) {
        progressView.setProgress(progress, animated: animated)
    }

    func setHidden(_ isHidden: Bool, animated: Bool, completion: (() -> Void)? = nil) {
        let completionHandler: () -> Void = { [weak self] in
            if isHidden {
                self?.setProgress(0, animated: false)
            }
            completion?()
        }

        // Check if the current hidden state already matches the desired state
        if self.isHidden == isHidden {
            return completionHandler()
        }

        if isHidden {
            animateVisibility(shouldHide: true, animated: animated, completion: completionHandler)
        } else {
            animateVisibility(shouldHide: false, animated: animated, completion: completionHandler)
        }
    }

    private func animateVisibility(shouldHide: Bool, animated: Bool, completion: (() -> Void)?) {
        isHidden = false

        let initialAlpha: CGFloat = shouldHide ? 1 : 0
        let finalAlpha: CGFloat = shouldHide ? 0 : 1

        if animated {
            alpha = initialAlpha

            UIView.animate(
                withDuration: Animation.visibilityDuration,
                animations: {
                    self.alpha = finalAlpha
                },
                completion: { _ in
                    if shouldHide {
                        self.isHidden = true
                    }

                    completion?()
                }
            )
        } else {
            alpha = finalAlpha

            if shouldHide {
                isHidden = true
            }

            completion?()
        }
    }
}

extension StepToolbarProgressView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {}

    func addSubviews() {
        addSubview(progressView)
    }

    func makeConstraints() {
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.height.equalTo(appearance.height)
        }
    }
}

// MARK: - Preview -

#if DEBUG
@available(iOS 17, *)
#Preview {
    let view = StepToolbarProgressView()
    view.setHidden(false, animated: true, completion: nil)
    return view
}
#endif
