import Lottie
import SnapKit
import UIKit

extension SpacebotWowAnimationView {
    struct Appearance {
        let animationViewAlphaHidden: CGFloat = 0
        let animationViewAlphaVisible: CGFloat = 1
    }
}

final class SpacebotWowAnimationView: UIView {
    private static let initialAnimationProgressTime: AnimationProgressTime = 0

    let appearance: Appearance

    private lazy var animationView: LottieAnimationView = {
        let view = LottieAnimationView()
        view.alpha = appearance.animationViewAlphaHidden
        return view
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

    func play(completion: LottieCompletionBlock? = nil) {
        animationView.alpha = appearance.animationViewAlphaVisible
        animationView.play { [weak self] completed in
            completion?(completed)

            guard let strongSelf = self else {
                return
            }

            strongSelf.animationView.alpha = strongSelf.appearance.animationViewAlphaHidden
            strongSelf.animationView.currentProgress = 0
        }
    }

    func stop() {
        animationView.stop()
    }

    func resetCurrentProgress() {
        animationView.currentProgress = Self.initialAnimationProgressTime
    }

    private func setLottieAnimation() {
        DotLottieFile.named(LottieAnimations.spacebotProgressBarWow.light) { [weak animationView] result in
            guard let animationView else {
                return print("SpacebotWowAnimationView: animationView is deallocated")
            }

            switch result {
            case .success(let dotLottieFile):
                animationView.loadAnimation(from: dotLottieFile)
                animationView.contentMode = .scaleAspectFit
                animationView.loopMode = .playOnce
            case .failure(let error):
                assertionFailure("SpacebotWowAnimationView: failed load dotLottieFile with error: \(error)")
            }
        }
    }
}

extension SpacebotWowAnimationView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        isUserInteractionEnabled = false
        setLottieAnimation()
    }

    func addSubviews() {
        addSubview(animationView)
    }

    func makeConstraints() {
        animationView.translatesAutoresizingMaskIntoConstraints = false
        animationView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

#if DEBUG
@available(iOS 17, *)
#Preview {
    let view = SpacebotWowAnimationView()
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        view.play()
    }
    return view
}
#endif
