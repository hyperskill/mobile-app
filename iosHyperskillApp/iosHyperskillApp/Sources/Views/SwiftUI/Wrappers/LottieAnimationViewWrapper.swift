import Lottie
import SnapKit
import SwiftUI

struct LottieAnimationFileName {
    let light: String
    let dark: String
}

struct LottieAnimationViewWrapper: UIViewRepresentable {
    let fileName: LottieAnimationFileName

    func makeUIView(context: Context) -> some UIView {
        let view = UIView(frame: .zero)

        let fileNameForCurrentInterfaceStyle = view.isDarkInterfaceStyle ? fileName.dark : fileName.light

        let animationView = LottieAnimationView()
        animationView.animation = LottieAnimation.named(fileNameForCurrentInterfaceStyle)
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

    func updateUIView(_ uiView: UIViewType, context: Context) {}
}

struct LottieAnimationViewWrapper_Previews: PreviewProvider {
    static var previews: some View {
        LottieAnimationViewWrapper(
            fileName: LottieAnimations.parsonsProblemOnboarding
        )
        .padding()

        LottieAnimationViewWrapper(
            fileName: LottieAnimations.parsonsProblemOnboarding
        )
        .padding()
        .preferredColorScheme(.dark)
    }
}
