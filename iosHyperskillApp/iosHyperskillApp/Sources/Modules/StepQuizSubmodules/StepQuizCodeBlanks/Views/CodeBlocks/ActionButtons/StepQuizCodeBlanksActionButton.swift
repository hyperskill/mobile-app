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
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

extension StepQuizCodeBlanksActionButton {
    static func delete(action: @escaping () -> Void) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(imageSystemName: "delete.left", action: action)
    }

    static func enter(action: @escaping () -> Void) -> StepQuizCodeBlanksActionButton {
        StepQuizCodeBlanksActionButton(imageSystemName: "return", action: action)
    }

    static func space(
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
            StepQuizCodeBlanksActionButton.delete(action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
            StepQuizCodeBlanksActionButton.space(action: {})
            StepQuizCodeBlanksActionButton.decreaseIndentLevel(action: {})
        }

        HStack {
            StepQuizCodeBlanksActionButton.delete(action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
            StepQuizCodeBlanksActionButton.space(action: {})
            StepQuizCodeBlanksActionButton.decreaseIndentLevel(action: {})
        }
        .disabled(true)
    }
}
#endif
