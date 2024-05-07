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

        let spacebotWowSize = CGSize(width: 108, height: 108)
    }

    enum Animation {
        static let visibilityDuration: TimeInterval = 0.3

        static let progressUpdateMinDuration: TimeInterval = 0.3
        static let progressUpdateMaxDuration: TimeInterval = 1.0
        static let progressUpdateScaleFactor: Double = 2.0
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

    private var isAnimationPlaying = false

    private(set) var progress: Float?

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

    func handleOrientationChange() {
        updateSpacebotRocketOffset()
    }

    @objc
    private func didTapSpacebotRocketAnimationView() {
        spacebotWowAnimationView.play()
    }
}

// MARK: - StepToolbarProgressView (Progress) -

extension StepToolbarProgressView {
    func setProgress(_ progress: Float, animated: Bool, playWowAnimation: Bool = false) {
        guard self.progress != progress else {
            return
        }

        let progressDelta = abs(progress - (self.progress ?? 0))
        self.progress = progress

        if animated {
            startAnimationSequence(
                playWowAnimation: playWowAnimation,
                progress: progress,
                progressDelta: progressDelta
            )
        } else {
            updateProgressImmediately(progress)
        }
    }

    private func startAnimationSequence(playWowAnimation: Bool, progress: Float, progressDelta: Float) {
        guard !isAnimationPlaying else {
            return
        }
        isAnimationPlaying = true

        if playWowAnimation {
            playRocketAndWowAnimations(progress: progress, progressDelta: progressDelta)
        } else {
            animateProgress(progress, progressDelta: progressDelta) { [weak self] in
                self?.isAnimationPlaying = false
            }
        }
    }

    private func playRocketAndWowAnimations(progress: Float, progressDelta: Float) {
        spacebotRocketAnimationView.playAppearance { [weak self] _ in
            self?.animateProgress(progress, progressDelta: progressDelta) { [weak self] in
                self?.spacebotRocketAnimationView.playDisappearance { [weak self] _ in
                    self?.spacebotWowAnimationView.play()
                    self?.isAnimationPlaying = false
                }
            }
        }
    }

    private func animateProgress(_ progress: Float, progressDelta: Float, completion: (() -> Void)? = nil) {
        // Calculate animation duration based on progressDelta
        let animationDuration = min(
            max(Double(progressDelta) * Animation.progressUpdateScaleFactor, Animation.progressUpdateMinDuration),
            Animation.progressUpdateMaxDuration
        )

        UIView.animate(
            withDuration: animationDuration,
            animations: { [weak self] in
                guard let self else {
                    return
                }

                self.progressView.progress = progress
                self.progressView.layoutIfNeeded()

                self.updateSpacebotRocketOffset()
                self.layoutIfNeeded()
            },
            completion: { _ in
                completion?()
            }
        )
    }

    private func updateSpacebotRocketOffset() {
        let normalizedProgress = min(
            max(CGFloat(progressView.progress), appearance.spacebotRocketMinProgress),
            appearance.spacebotRocketMaxProgress
        )
        let offset = UIScreen.main.bounds.width * CGFloat(normalizedProgress)
            - (appearance.spacebotRocketSize.width / 2)
            - (appearance.spacebotHeadSize.width / 2)
        spacebotRocketAnimationViewLeadingConstraint?.update(offset: offset)
    }

    private func updateProgressImmediately(_ progress: Float) {
        progressView.progress = progress
        updateSpacebotRocketOffset()
    }
}

// MARK: - StepToolbarProgressView (Visibility) -

extension StepToolbarProgressView {
    func setHidden(_ isHidden: Bool, animated: Bool, completion: (() -> Void)? = nil) {
        let completionHandler: () -> Void = { [weak self] in
            if isHidden {
                self?.setProgress(0, animated: false)
                self?.progress = nil
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

// MARK: - StepToolbarProgressView: ProgrammaticallyInitializableViewProtocol -

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
    styledNavigationController.setProgress(0, animated: false)
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        styledNavigationController.setProgress(0.5, animated: true)
    }
    return styledNavigationController
}

@available(iOS 17, *)
#Preview("Full") {
    let styledNavigationController = makeStyledNavigationController()
    styledNavigationController.setProgress(0, animated: false)
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        styledNavigationController.setProgress(1, animated: true)
    }
    return styledNavigationController
}
#endif
