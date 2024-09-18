import SwiftUI

extension StepQuizCodeBlanksActionButton {
    struct Appearance {
        var padding = LayoutInsets(
            horizontal: LayoutInsets.smallInset,
            vertical: LayoutInsets.smallInset / 2
        )

        let cornerRadius: CGFloat = 8
    }
}

struct StepQuizCodeBlanksActionButton: View {
    private(set) var appearance = Appearance()

    let imageSystemName: String

    var isAnimationEffectActive = false

    let action: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        Button(
            action: action,
            label: {
                Image(systemName: imageSystemName)
                    .imageScale(.large)
                    .padding(appearance.padding.edgeInsets)
                    .background(
                        Color(ColorPalette.primary)
                            .conditionalOpacity(isEnabled: isEnabled)
                    )
                    .foregroundColor(Color(ColorPalette.onPrimary))
                    .cornerRadius(appearance.cornerRadius)
                    .shineEffect(isActive: isEnabled && isAnimationEffectActive)
                    .pulseEffect(
                        shape: RoundedRectangle(cornerRadius: appearance.cornerRadius),
                        isActive: isEnabled && isAnimationEffectActive
                    )
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

extension StepQuizCodeBlanksActionButton {
    static func delete(
        isAnimationEffectActive: Bool,
        action: @escaping () -> Void
    ) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(
            imageSystemName: "delete.left",
            isAnimationEffectActive: isAnimationEffectActive,
            action: action
        )
    }

    static func enter(action: @escaping () -> Void) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(imageSystemName: "return", action: action)
    }

    static func space(
        isAnimationEffectActive: Bool,
        action: @escaping () -> Void
    ) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(
            appearance: .init(
                padding: LayoutInsets(
                    horizontal: LayoutInsets.smallInset,
                    vertical: 10.5
                )
            ),
            imageSystemName: "space",
            isAnimationEffectActive: isAnimationEffectActive,
            action: action
        )
    }

    static func decreaseIndentLevel(action: @escaping () -> Void) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(
            appearance: .init(
                padding: LayoutInsets(
                    horizontal: LayoutInsets.smallInset,
                    vertical: 5.5
                )
            ),
            imageSystemName: "arrow.left.to.line",
            action: action
        )
    }
}

#if DEBUG
#Preview {
    VStack {
        HStack {
            StepQuizCodeBlanksActionButton.delete(isAnimationEffectActive: false, action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
            StepQuizCodeBlanksActionButton.space(isAnimationEffectActive: false, action: {})
            StepQuizCodeBlanksActionButton.decreaseIndentLevel(action: {})
        }

        HStack {
            StepQuizCodeBlanksActionButton.delete(isAnimationEffectActive: false, action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
            StepQuizCodeBlanksActionButton.space(isAnimationEffectActive: false, action: {})
            StepQuizCodeBlanksActionButton.decreaseIndentLevel(action: {})
        }
        .disabled(true)
    }
}
#endif
