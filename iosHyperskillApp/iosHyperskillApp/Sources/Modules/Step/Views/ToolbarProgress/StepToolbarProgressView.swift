import Lottie
import SnapKit
import UIKit

extension StepToolbarProgressView {
    struct Appearance {
        let progressBarTrackTintColor = UIColor.dynamic(
            light: UIColor(red: 232.0 / 255.0, green: 232.0 / 255.0, blue: 232.0 / 255.0, alpha: 1.0),
            dark: UIColor(red: 50.0 / 255.0, green: 50.0 / 255.0, blue: 50.0 / 255.0, alpha: 1.0)
        )
        let progressBarProgressTintColor = ColorPalette.primary
        let progressBarHeight: CGFloat = 4

        let spacebotRocketSize = CGSize(width: 111, height: 60)
        let spacebotHeadSize = CGSize(width: 36, height: 36)
        let spacebotRocketMinProgress: CGFloat = 0.043
        let spacebotRocketMaxProgress: CGFloat = 0.95

        let spacebotWowSize = CGSize(width: 72, height: 72)
    }

    enum Animation {
        static let visibilityDuration: TimeInterval = 0.3
    }
}

final class StepToolbarProgressView: UIView {
    let appearance: Appearance

    private lazy var progressView: UIProgressView = {
        let progressView = UIProgressView(progressViewStyle: .bar)
        progressView.trackTintColor = appearance.progressBarTrackTintColor
        progressView.progressTintColor = appearance.progressBarProgressTintColor
        return progressView
    }()

    private lazy var spacebotWowAnimationView = SpacebotWowAnimationView()

    private lazy var spacebotRocketAnimationView = SpacebotRocketAnimationView()
    private var spacebotRocketAnimationViewLeadingConstraint: Constraint?

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
        updateSpacebotRocketOffset()
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

    private func updateSpacebotRocketOffset() {
        let normalizedProgress = min(
            max(CGFloat(progressView.progress), appearance.spacebotRocketMinProgress),
            appearance.spacebotRocketMaxProgress
        )
        let offset = bounds.size.width * CGFloat(normalizedProgress)
            - (appearance.spacebotRocketSize.width / 2)
            - (appearance.spacebotHeadSize.width / 2)
        spacebotRocketAnimationViewLeadingConstraint?.update(offset: offset)
    }

    @objc
    private func didTapSpacebotRocketAnimationView() {
        spacebotWowAnimationView.play()
    }
}

extension StepToolbarProgressView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        spacebotRocketAnimationView.onTap = { [weak self] in
            guard let self else {
                return
            }

            self.spacebotWowAnimationView.play()
        }
    }

    func addSubviews() {
        addSubview(progressView)
        addSubview(spacebotWowAnimationView)
        addSubview(spacebotRocketAnimationView)
    }

    func makeConstraints() {
        progressView.translatesAutoresizingMaskIntoConstraints = false
        progressView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.height.equalTo(appearance.progressBarHeight)
        }

        spacebotWowAnimationView.translatesAutoresizingMaskIntoConstraints = false
        spacebotWowAnimationView.snp.makeConstraints { make in
            make.size.equalTo(appearance.spacebotWowSize)
            make.centerY.equalTo(spacebotRocketAnimationView.snp.centerY)
            make.centerX.equalTo(spacebotRocketAnimationView.snp.centerX).offset(appearance.spacebotHeadSize.width / 2)
        }

        spacebotRocketAnimationView.translatesAutoresizingMaskIntoConstraints = false
        spacebotRocketAnimationView.snp.makeConstraints { make in
            make.centerY.equalTo(progressView.snp.centerY)
            spacebotRocketAnimationViewLeadingConstraint = make.leading.equalToSuperview().constraint
        }
    }
}

// MARK: - Preview -

#if DEBUG
func makeStyledNavigationController() -> StyledNavigationController {
    let rootViewController = UIViewController()
    rootViewController.title = "Progress"
    return StyledNavigationController(rootViewController: rootViewController)
}

@available(iOS 17, *)
#Preview("Zero") {
    let styledNavigationController = makeStyledNavigationController()
    styledNavigationController.setProgress(0, animated: true)
    return styledNavigationController
}

@available(iOS 17, *)
#Preview("Half") {
    let styledNavigationController = makeStyledNavigationController()
    styledNavigationController.setProgress(0.5, animated: true)
    return styledNavigationController
}

@available(iOS 17, *)
#Preview("Full") {
    let styledNavigationController = makeStyledNavigationController()
    styledNavigationController.setProgress(1, animated: true)
    return styledNavigationController
}
#endif
