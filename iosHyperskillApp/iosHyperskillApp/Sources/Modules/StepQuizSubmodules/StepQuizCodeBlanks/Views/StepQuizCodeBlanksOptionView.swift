import SwiftUI

extension StepQuizCodeBlanksOptionView {
    enum Appearance {
        static let insets = LayoutInsets(horizontal: 12, vertical: LayoutInsets.smallInset)
        static let minWidth: CGFloat = 48
        static let minHeight: CGFloat = 40
    }
}

struct StepQuizCodeBlanksOptionView: View {
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
                color: isActive ? StepQuizCodeBlanksAppearance.activeBorderColor : .border
            )
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksOptionView(text: "print", isActive: false)
        StepQuizCodeBlanksOptionView(text: "There is a cat on the keyboard, it is true", isActive: true)
        StepQuizCodeBlanksOptionView(text: "Typing messages out of the blue", isActive: true)
    }
}
#endif
