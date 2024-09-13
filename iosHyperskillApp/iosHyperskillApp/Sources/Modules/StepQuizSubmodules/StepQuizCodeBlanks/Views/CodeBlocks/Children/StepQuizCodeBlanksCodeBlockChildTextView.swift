import SwiftUI

extension StepQuizCodeBlanksCodeBlockChildTextView {
    enum Appearance {
        static let insets = LayoutInsets(horizontal: 12, vertical: LayoutInsets.smallInset)
        static let minWidth: CGFloat = 48
        static let minHeight: CGFloat = 40
        static let cornerRadius: CGFloat = 8
    }
}

struct StepQuizCodeBlanksCodeBlockChildTextView: View {
    let text: String

    let isActive: Bool

    var body: some View {
        Text(text)
            .font(StepQuizCodeBlanksAppearance.blankFont)
            .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)
            .padding(Appearance.insets.edgeInsets)
            .frame(minWidth: Appearance.minWidth, minHeight: Appearance.minHeight)
            .background(Color(ColorPalette.background))
            .addBorder(
                color: isActive ? StepQuizCodeBlanksAppearance.activeBorderColor : .border,
                cornerRadius: Appearance.cornerRadius
            )
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksCodeBlockChildTextView(text: "print", isActive: false)
        StepQuizCodeBlanksCodeBlockChildTextView(text: "There is a cat on the keyboard, it is true", isActive: true)
        StepQuizCodeBlanksCodeBlockChildTextView(text: "Typing messages out of the blue", isActive: true)
    }
}
#endif
