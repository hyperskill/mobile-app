import SwiftUI

extension StepQuizChoiceElementView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let minHeight: CGFloat = 24
        let checkboxIndicatorWidthHeight: CGFloat = 18
        let radioIndicatorWidthHeight: CGFloat = 20
    }
}

struct StepQuizChoiceElementView: View {
    private(set)var appearance = Appearance()

    var isSelected: Binding<Bool>

    let text: String

    var isMultipleChoice: Bool

    var selectChoice: (Binding<Bool>) -> Void

    var body: some View {
        Button(
            action: {
                selectChoice(isSelected)
            },
            label: {
                HStack(spacing: appearance.interItemSpacing) {
                    buildIndicator(
                        isSelected: isSelected,
                        onClick: {
                            selectChoice(isSelected)
                        }
                    )

                    LatexView(text: .constant(text), configuration: .quizContent())
                }
            }
        )
        .frame(minHeight: appearance.minHeight)
    }

    @ViewBuilder
    private func buildIndicator(isSelected: Binding<Bool>, onClick: @escaping () -> Void) -> some View {
        if isMultipleChoice {
            CheckboxButton(isSelected: isSelected, onClick: onClick)
                .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
        } else {
            RadioButton(isSelected: isSelected, onClick: onClick)
                .frame(widthHeight: appearance.radioIndicatorWidthHeight)
        }
    }
}

struct StepQuizChoiceElementView_Previews: PreviewProvider {
    static var previews: some View {
        let lambda = { (selectedChoice: Binding<Bool>) -> Void in
            selectedChoice.wrappedValue.toggle()
        }

        return Group {
            StepQuizChoiceElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            StepQuizChoiceElementView(
                isSelected: .constant(false),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            StepQuizChoiceElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: false,
                selectChoice: lambda
            )
            StepQuizChoiceElementView(
                isSelected: .constant(false),
                text: "Some option",
                isMultipleChoice: false,
                selectChoice: lambda
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
