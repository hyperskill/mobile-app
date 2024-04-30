import Lottie
import SnapKit
import UIKit

extension SpacebotRocketAnimationView {
    struct Appearance {}
}

final class SpacebotRocketAnimationView: UIView {
    let appearance: Appearance

    private lazy var animationView = LottieAnimationView()

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

    func playAppearance(completion: LottieCompletionBlock? = nil) {
        animationView.animationSpeed = -2.5
        animationView.play(completion: completion)
    }

    func playDisappearance(completion: LottieCompletionBlock? = nil) {
        animationView.animationSpeed = 1.5
        animationView.play(completion: completion)
    }

    private func setLottieAnimation() {
        let dotLottieName = isDarkInterfaceStyle
            ? LottieAnimations.spacebotProgressBarRocket.dark
            : LottieAnimations.spacebotProgressBarRocket.light

        DotLottieFile.named(dotLottieName) { [weak animationView] result in
            guard let animationView else {
                return print("SpacebotRocketAnimationView: animationView is deallocated")
            }

            switch result {
            case .success(let dotLottieFile):
                animationView.loadAnimation(from: dotLottieFile)
                animationView.contentMode = .scaleAspectFit
                animationView.loopMode = .playOnce
            case .failure(let error):
                assertionFailure("SpacebotRocketAnimationView: failed load dotLottieFile with error: \(error)")
            }
        }
    }
}

extension SpacebotRocketAnimationView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
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
#Preview("Appearance") {
    let view = SpacebotRocketAnimationView()
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        view.playAppearance()
    }
    return view
}

@available(iOS 17, *)
#Preview("Disappearance") {
    let view = SpacebotRocketAnimationView()
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        view.playDisappearance()
    }
    return view
}

@available(iOS 17, *)
#Preview("Both") {
    let view = SpacebotRocketAnimationView()
    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        view.playAppearance { _ in
            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                view.playDisappearance()
            }
        }
    }
    return view
}
#endif
