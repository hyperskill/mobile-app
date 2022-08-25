import SwiftUI

extension StepQuizTableSelectColumnsColumnView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let checkboxIndicatorWidthHeight: CGFloat = 18
        let radioIndicatorWidthHeight: CGFloat = 20

        let minHeight: CGFloat = 44
    }
}

struct StepQuizTableSelectColumnsColumnView: View {
    private(set) var appearance = Appearance()

    var isSelected: Binding<Bool>

    let text: String

    var isMultipleChoice: Bool

    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: appearance.interItemSpacing) {
                buildIndicator(isSelected: isSelected, onTap: onTap)

                LatexView(text: .constant(text), configuration: .quizContent())
            }
        }
        .frame(maxWidth: .infinity, minHeight: appearance.minHeight)
    }

    @ViewBuilder
    private func buildIndicator(isSelected: Binding<Bool>, onTap: @escaping () -> Void) -> some View {
        if isMultipleChoice {
            CheckboxButton(
                appearance: .init(backgroundUnselectedColor: .clear),
                isSelected: isSelected,
                onClick: onTap
            )
            .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
        } else {
            RadioButton(
                appearance: .init(indicatorUnselectedColor: .clear, backgroundColor: .clear),
                isSelected: isSelected,
                onClick: onTap
            )
            .frame(widthHeight: appearance.radioIndicatorWidthHeight)
        }
    }
}

struct StepQuizTableSelectColumnsColumnView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizTableSelectColumnsColumnView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: false,
                onTap: {}
            )
            StepQuizTableSelectColumnsColumnView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: true,
                onTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
