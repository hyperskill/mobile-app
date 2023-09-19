import SwiftUI

private struct ShineEffectViewModifier: ViewModifier {
    static let defaultForegroundColor = Color.white

    static let defaultAnimation = Animation
        .easeIn(duration: 1)
        .delay(1.5)
        .repeatForever(autoreverses: false)

    static let defaultGradient: LinearGradient = {
        let foregroundColor = Color.white
        let transparentColor = foregroundColor.opacity(0)
        let peakColor = foregroundColor.opacity(0.5)

        let gradientStops: [Gradient.Stop] = [
            .init(color: transparentColor, location: 0),
            .init(color: peakColor, location: 0.5),
            .init(color: transparentColor, location: 1)
        ]

        return LinearGradient(
            gradient: .init(stops: gradientStops),
            startPoint: .top,
            endPoint: .bottom
        )
    }()

    static let defaultAngle = Angle(degrees: 90)

    private let foregroundColor: Color
    private let animation: Animation
    private let gradient: LinearGradient
    private let angle: Angle

    @State private var isInitialState = true

    init(
        foregroundColor: Color = Self.defaultForegroundColor,
        animation: Animation = Self.defaultAnimation,
        gradient: LinearGradient = Self.defaultGradient,
        angle: Angle = Self.defaultAngle
    ) {
        self.foregroundColor = foregroundColor
        self.animation = animation
        self.gradient = gradient
        self.angle = angle
    }

    func body(content: Content) -> some View {
        content
            .overlay(
                GeometryReader { geometryProxy in
                    foregroundColor.opacity(0.5)
                        .mask(Rectangle().fill(gradient))
                        .padding(-computeSize(geometryProxy))
                        .rotationEffect(angle)
                        .offset(x: computeOffset(geometryProxy), y: 0)
                }.mask(content).allowsHitTesting(false)
            )
            .onAppear {
                withAnimation(animation) {
                    isInitialState = false
                }
            }
    }

    private func computeOffset(_ geometryProxy: GeometryProxy) -> CGFloat {
        let size = computeSize(geometryProxy)
        return (isInitialState ? -size : size) * 2
    }

    private func computeSize(_ geometryProxy: GeometryProxy) -> CGFloat {
        max(geometryProxy.size.width, geometryProxy.size.height)
    }
}

extension View {
    /// An effect that highlights the view with a shine moving over the view.
    @ViewBuilder
    func shineEffect(
        isActive: Bool = true,
        foregroundColor: Color = ShineEffectViewModifier.defaultForegroundColor,
        animation: Animation = ShineEffectViewModifier.defaultAnimation,
        gradient: LinearGradient = ShineEffectViewModifier.defaultGradient,
        angle: Angle = ShineEffectViewModifier.defaultAngle
    ) -> some View {
        if isActive {
            modifier(
                ShineEffectViewModifier(
                    foregroundColor: foregroundColor,
                    animation: animation,
                    gradient: gradient,
                    angle: angle
                )
            )
        } else {
            self
        }
    }
}

#if DEBUG
struct ShineEffect_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            Button("Continue", action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .frame(width: 120)
                .shineEffect()

            Button("Continue", action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .shineEffect()
        }
        .padding()
    }
}
#endif
