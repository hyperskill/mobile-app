import SwiftUI

struct StepQuizCodeFixCodeMistakesBadge: View {
    var body: some View {
        Text(Strings.StepQuizCode.badgeFixMistakes)
            .font(.headline)
            .foregroundColor(.primaryText)
            .padding(LayoutInsets.defaultInset)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color(ColorPalette.overlayVioletAlpha12))
            .cornerRadius(LayoutInsets.smallInset)
    }
}

#Preview {
    StepQuizCodeFixCodeMistakesBadge()
        .padding()
}

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
