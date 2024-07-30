import SwiftUI

struct StepQuizCodeBlanksActionButton: View {
    let imageSystemName: String

    let action: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        Button(
            action: action,
            label: {
                Image(systemName: imageSystemName)
                    .imageScale(.large)
                    .padding(.vertical, LayoutInsets.smallInset / 2)
                    .padding(.horizontal, LayoutInsets.smallInset)
                    .background(
                        Color(ColorPalette.primary)
                            .conditionalOpacity(isEnabled: isEnabled)
                    )
                    .foregroundColor(Color(ColorPalette.onPrimary))
                    .cornerRadius(8)
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
}

#if DEBUG
#Preview {
    VStack {
        HStack {
            StepQuizCodeBlanksActionButton.delete(action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
        }

        HStack {
            StepQuizCodeBlanksActionButton.delete(action: {})
            StepQuizCodeBlanksActionButton.enter(action: {})
        }
        .disabled(true)
    }
}
#endif
