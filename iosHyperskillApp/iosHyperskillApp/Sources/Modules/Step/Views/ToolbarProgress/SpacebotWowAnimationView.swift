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

    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        performBlockIfAppearanceChanged(from: previousTraitCollection, block: setLottieAnimation)
    }

    func play(completion: LottieCompletionBlock? = nil) {
        animationView.alpha = appearance.animationViewAlphaVisible
        animationView.play { [weak self] completed in
            completion?(completed)

            guard let self else {
                return
            }

            self.animationView.alpha = self.appearance.animationViewAlphaHidden
            self.animationView.currentProgress = 0
        }
    }

    private func setLottieAnimation() {
        let dotLottieName = isDarkInterfaceStyle
            ? LottieAnimations.spacebotProgressBarWow.dark
            : LottieAnimations.spacebotProgressBarWow.light

        DotLottieFile.named(dotLottieName) { [weak animationView] result in
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
