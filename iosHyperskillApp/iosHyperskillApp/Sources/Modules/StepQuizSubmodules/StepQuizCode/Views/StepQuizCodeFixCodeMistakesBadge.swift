import SwiftUI

struct StepQuizCodeFixCodeMistakesBadge: View {
    var onQuestionmarkButtonTap: () -> Void

    var body: some View {
        HStack {
            Text(Strings.StepQuizCode.badgeFixMistakes)
                .foregroundColor(.primaryText)

            Spacer()

            Button(
                action: onQuestionmarkButtonTap,
                label: {
                    Image(systemName: "questionmark.circle")
                        .foregroundColor(.secondaryText)
                }
            )
        }
        .font(.headline)
        .padding(LayoutInsets.defaultInset)
        .frame(maxWidth: .infinity)
        .background(Color(ColorPalette.overlayVioletAlpha12))
        .cornerRadius(LayoutInsets.smallInset)
    }
}

#if DEBUG
#Preview {
    StepQuizCodeFixCodeMistakesBadge(onQuestionmarkButtonTap: {})
        .padding()
}
#endif

// MARK: - SwiftUI Environment Value -

private struct FixCodeMistakesBadgeVisibleKey: EnvironmentKey {
    static let defaultValue = false
}

extension EnvironmentValues {
    var isFixCodeMistakesBadgeVisible: Bool {
        get {
            self[FixCodeMistakesBadgeVisibleKey.self]
        }
        set {
            self[FixCodeMistakesBadgeVisibleKey.self] = newValue
        }
    }
}
