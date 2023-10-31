import Lottie
import SnapKit
import SwiftUI

struct LottieAnimationFileName {
    let light: String
    let dark: String
}

struct LottieAnimationViewWrapper: UIViewRepresentable {
    let fileName: LottieAnimationFileName

    @Environment(\.colorScheme) var colorScheme

    func makeUIView(context: Context) -> some UIView {
        let view = UIView(frame: .zero)

        let animationView = LottieAnimationView()
        animationView.animation = getLottieAnimationForCurrentColorScheme()
        animationView.contentMode = .scaleAspectFit
        animationView.loopMode = .autoReverse
        animationView.play()

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
            animationView.animation = getLottieAnimationForCurrentColorScheme()
            animationView.play()
        }

        context.coordinator.previousColorScheme = colorScheme
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(initialColorScheme: colorScheme)
    }

    private func getLottieAnimationForCurrentColorScheme() -> LottieAnimation? {
        switch colorScheme {
        case .dark:
            LottieAnimation.named(fileName.dark)
        default:
            LottieAnimation.named(fileName.light)
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

#Preview("Light") {
    LottieAnimationViewWrapper(
        fileName: LottieAnimations.parsonsProblemOnboarding
    )
    .padding()
    .preferredColorScheme(.light)
}

#Preview("Dark") {
    LottieAnimationViewWrapper(
        fileName: LottieAnimations.parsonsProblemOnboarding
    )
    .padding()
    .preferredColorScheme(.dark)
}
