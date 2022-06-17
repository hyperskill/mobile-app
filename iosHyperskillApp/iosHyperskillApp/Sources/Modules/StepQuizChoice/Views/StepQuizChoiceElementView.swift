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
    private(set)var appearance = Appearance()

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

                LatexView(text: .constant(text), configuration: .quizContent())
            }
        }
        .frame(minHeight: appearance.minHeight)
        .frame(maxWidth: .infinity)
    }
}

struct StepQuizChoiceElementView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizChoiceElementView(
                isSelected: true,
                text: "Some option",
                isMultipleChoice: true,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: false,
                text: "Some option",
                isMultipleChoice: true,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: true,
                text: "Some option",
                isMultipleChoice: false,
                onTap: {}
            )
            StepQuizChoiceElementView(
                isSelected: false,
                text: "Some option",
                isMultipleChoice: false,
                onTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
