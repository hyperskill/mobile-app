import SwiftUI

private struct JiggleEffectViewModifier: ViewModifier {
    let amount: Double

    @State private var isJiggling = false

    func body(content: Content) -> some View {
        content
            .rotationEffect(.degrees(isJiggling ? amount : 0))
            .animation(
                .easeInOut(duration: randomize(interval: 0.14, withVariance: 0.025))
                .repeatForever(autoreverses: true),
                value: isJiggling
            )
            .animation(
                .easeInOut(duration: randomize(interval: 0.18, withVariance: 0.025))
                .repeatForever(autoreverses: true),
                value: isJiggling
            )
            .onAppear {
                isJiggling.toggle()
            }
    }

    private func randomize(interval: TimeInterval, withVariance variance: Double) -> TimeInterval {
        interval + variance * (Double.random(in: 500...1_000) / 500)
    }
}

extension View {
    @ViewBuilder
    func jiggleEffect(amount: Double = 2, isActive: Bool = true) -> some View {
        if isActive {
            modifier(JiggleEffectViewModifier(amount: amount))
        } else {
            self
        }
    }
}

//#if DEBUG
//@available(iOS 17.0, *)
//#Preview {
//    @Previewable @State var isJiggling = false
//
//    Button {
//        isJiggling.toggle()
//    } label: {
//        Text("Retry")
//    }
//    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
//    .jiggleEffect(amount: 2, isActive: isJiggling)
//    .padding()
//}
//#endif
