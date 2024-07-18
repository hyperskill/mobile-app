import SwiftUI

struct StepQuizCodeBlanksPrintInstructionView: View {
    let isActive: Bool

    let output: String?

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                Text("print(")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                if let output, !output.isEmpty {
                    StepQuizCodeBlanksOptionView(text: output, isActive: isActive)
                } else {
                    StepQuizCodeBlanksBlankView(style: .small, isActive: isActive)
                }

                Text(")")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)
            }
            .padding(.horizontal, LayoutInsets.defaultInset)
            .padding(.vertical, LayoutInsets.smallInset)
            .background(Color(ColorPalette.violet400Alpha7))
            .cornerRadius(8)
            .animation(.default, value: isActive)
            .animation(.default, value: output)
            .padding(.horizontal)
        }
        .scrollBounceBehaviorBasedOnSize(axes: .horizontal)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksPrintInstructionView(isActive: false, output: "")
        StepQuizCodeBlanksPrintInstructionView(isActive: true, output: "")
        StepQuizCodeBlanksPrintInstructionView(isActive: true, output: "There is a cat on the keyboard, it is true")
        StepQuizCodeBlanksPrintInstructionView(isActive: false, output: "There is a cat on the keyboard, it is true")
    }
    .frame(maxWidth: .infinity)
    .padding()
}
#endif
