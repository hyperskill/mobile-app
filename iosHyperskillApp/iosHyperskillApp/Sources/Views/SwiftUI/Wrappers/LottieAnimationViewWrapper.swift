import Lottie
import SnapKit
import SwiftUI

struct LottieAnimationFileName {
    let light: String
    let dark: String
}

struct LottieAnimationViewWrapper: UIViewRepresentable {
    let fileName: LottieAnimationFileName

    var loopMode: LottieLoopMode = .autoReverse
    var animationSpeed: CGFloat = 1

    @Environment(\.colorScheme) var colorScheme

    func makeUIView(context: Context) -> some UIView {
        let view = UIView(frame: .zero)

        let animationView = LottieAnimationView()
        setLottieAnimation(to: animationView)

        view.addSubview(animationView)

        animationView.translatesAutoresizingMaskIntoConstraints = false
        animationView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.width.height.equalToSuperview()
        }

        return view
    }

    func updateUIView(_ uiView: UIViewType, context: Context) {
        guard let animationView = uiView.subviews.first as? LottieAnimationView else {
            return assertionFailure("LottieAnimationViewWrapper: animationView is not found")
        }

        if context.coordinator.previousColorScheme != colorScheme {
            setLottieAnimation(to: animationView)
        }

        context.coordinator.previousColorScheme = colorScheme
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(initialColorScheme: colorScheme)
    }

    private func setLottieAnimation(to animationView: LottieAnimationView) {
        let name = colorScheme == .dark ? fileName.dark : fileName.light
        DotLottieFile.named(name) { [weak animationView] result in
            guard let animationView else {
                return print("LottieAnimationViewWrapper: animationView is deallocated")
            }

            switch result {
            case .success(let dotLottieFile):
                animationView.loadAnimation(from: dotLottieFile)
                animationView.contentMode = .scaleAspectFit
                animationView.loopMode = loopMode
                animationView.animationSpeed = animationSpeed
                animationView.play()
            case .failure(let error):
                assertionFailure("LottieAnimationViewWrapper: failed load dotLottieFile with error: \(error)")
            }
        }
    }
}

extension LottieAnimationViewWrapper {
    class Coordinator {
        var previousColorScheme: ColorScheme?

        init(initialColorScheme: ColorScheme) {
            self.previousColorScheme = initialColorScheme
        }
    }
}

#if DEBUG
#Preview("Parsons problem onboarding") {
    LottieAnimationViewWrapper(
        fileName: LottieAnimations.parsonsProblemOnboarding
    )
    .padding()
}
#endif
