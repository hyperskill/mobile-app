import SwiftUI

enum PulseEffect {
    @available(iOS 17.0, *)
    fileprivate struct EffectViewModifier<EffectShape: Shape>: ViewModifier {
        var shape: EffectShape

        var scaleAmount = ScaleAmount.default

        var foregroundColor = Color(ColorPalette.primary)

        func body(content: Content) -> some View {
            content
                .background(
                    ZStack {
                        shape
                            .fill(foregroundColor)
                            .opacity(0.1)
                            .phaseAnimator(
                                [false, true],
                                content: { contentView, phase in
                                    contentView
                                        .scaleEffect(scaleAmount.scale(for: phase)[0])
                                },
                                animation: { _ in
                                    .easeOut(duration: 1)
                                }
                            )

                        shape
                            .fill(foregroundColor)
                            .opacity(0.15)
                            .phaseAnimator(
                                [false, true],
                                content: { contentView, phase in
                                    contentView
                                        .scaleEffect(scaleAmount.scale(for: phase)[1])
                                },
                                animation: { _ in
                                    .easeIn(duration: 1)
                                }
                            )

                        shape
                            .fill(foregroundColor)
                            .opacity(0.2)
                            .phaseAnimator(
                                [false, true],
                                content: { contentView, phase in
                                    contentView
                                        .scaleEffect(scaleAmount.scale(for: phase)[2])
                                },
                                animation: { _ in
                                    .easeInOut(duration: 1)
                                }
                            )
                    }
                )
        }
    }

    enum ScaleAmount {
        case small
        case `default`

        fileprivate func scale(for phase: Bool) -> [CGSize] {
            phase ? animatingSizes : notAnimatingSizes
        }

        fileprivate var animatingSizes: [CGSize] {
            switch self {
            case .small:
                [
                    CGSize(width: 0.85, height: 0.85),
                    CGSize(width: 0.75, height: 0.75),
                    CGSize(width: 0.7, height: 0.7)
                ]
            case .default:
                [
                    CGSize(width: 0.8, height: 0.8),
                    CGSize(width: 0.7, height: 0.7),
                    CGSize(width: 0.5, height: 0.5)
                ]
            }
        }

        fileprivate var notAnimatingSizes: [CGSize] {
            switch self {
            case .small:
                [
                    CGSize(width: 1.085, height: 1.2),
                    CGSize(width: 1.05, height: 1.1),
                    CGSize(width: 1.085, height: 1.3)
                ]
            case .default:
                [
                    CGSize(width: 1.2, height: 1.3),
                    CGSize(width: 1.1, height: 1.2),
                    CGSize(width: 1.2, height: 1.4)
                ]
            }
        }
    }
}

extension View {
    @ViewBuilder
    func pulseEffect<EffectShape: Shape>(
        shape: EffectShape,
        scaleAmount: PulseEffect.ScaleAmount = .default,
        isActive: Bool = true
    ) -> some View {
        if #available(iOS 17.0, *) {
            if isActive {
                modifier(
                    PulseEffect.EffectViewModifier(
                        shape: shape,
                        scaleAmount: scaleAmount
                    )
                )
            } else {
                self
            }
        } else {
            self
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        Button("Continue", action: {})
            .buttonStyle(.primary)
            .pulseEffect(
                shape: RoundedRectangle(cornerRadius: 13),
                scaleAmount: .small
            )
    }
    .padding()
}
#endif
