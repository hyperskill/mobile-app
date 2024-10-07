import SwiftUI

private struct BounceEffectViewModifier: ViewModifier {
    @State private var isBouncing = false

    func body(content: Content) -> some View {
        content
            .scaleEffect(isBouncing ? 0.95 : 1)
            .animation(
                .easeInOut(duration: 0.15)
                .repeatForever(autoreverses: true)
                .delay(0.33),
                value: isBouncing
            )
            .onAppear {
                isBouncing = true
            }
    }
}

extension View {
    @ViewBuilder
    func bounceEffect(isActive: Bool = true) -> some View {
        if isActive {
            modifier(BounceEffectViewModifier())
        } else {
            self
        }
    }
}

//#if DEBUG
//@available(iOS 17.0, *)
//#Preview {
//    @Previewable @State var isBouncing = false
//
//    Button {
//        isBouncing.toggle()
//    } label: {
//        Text("Retry")
//    }
//    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
//    .bounceEffect(isActive: isBouncing)
//    .padding()
//}
//#endif
