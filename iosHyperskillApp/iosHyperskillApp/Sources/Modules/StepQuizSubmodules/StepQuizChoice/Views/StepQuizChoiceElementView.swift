import SwiftUI

extension StepQuizChoiceElementView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset

        let minHeight: CGFloat = 24
        let checkboxIndicatorWidthHeight: CGFloat = 18
        let radioIndicatorWidthHeight: CGFloat = 20
    }
}

struct StepQuizChoiceElementView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool

    let text: String

    let isMultipleChoice: Bool

    let onTap: (() -> Void)

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: appearance.interItemSpacing) {
                if isMultipleChoice {
                    CheckboxButton(isSelected: .constant(isSelected), onClick: onTap)
                        .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
                } else {
                    RadioButton(isSelected: .constant(isSelected), onClick: onTap)
                        .frame(widthHeight: appearance.radioIndicatorWidthHeight)
                }

                LatexView(
                    text: text,
                    configuration: .quizContent()
                )
            }
        }
        .frame(minHeight: appearance.minHeight)
        .frame(maxWidth: .infinity)
    }
}

struct StepQuizChoiceElementView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            StepQuizChoiceElementView(
                isSelected: true,
                text: "Your program needs a value and can't work without one",
                isMultipleChoice: true,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: false,
                text: "Your program needs three possible values for a variable, maybe use nullable Boolean?",
                isMultipleChoice: true,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: true,
                text: "Your program can obtain a value in the future, but there is no reasonable value now",
                isMultipleChoice: false,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: false,
                text: "Your program doesn't need a value",
                isMultipleChoice: false,
                onTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
