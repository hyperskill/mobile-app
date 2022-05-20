import SwiftUI

extension ChoiceQuizElementView {
    struct Appearance {
        var interItemSpacing = LayoutInsets.smallInset

        var minHeight: CGFloat = 24
        var checkboxIndicatorWidthHeight: CGFloat = 18
        var radioIndicatorWidthHeight: CGFloat = 20
    }
}

struct ChoiceQuizElementView: View {
    var isSelected: Binding<Bool>

    let text: String

    var isMultipleChoice: Bool

    var selectChoice: (Binding<Bool>) -> Void

    private(set)var appearance = Appearance()

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

                    Text(text)
                        .font(.body)
                        .foregroundColor(.primaryText)
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

struct ChoiceQuizElementView_Previews: PreviewProvider {
    static var previews: some View {
        let lambda = { (selectedChoice: Binding<Bool>) -> Void in selectedChoice.wrappedValue.toggle() }

        return Group {
            ChoiceQuizElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
                isSelected: .constant(false),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: false,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
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
