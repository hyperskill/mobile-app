import SwiftUI

extension LegacyStepQuizTableSelectColumnsColumnView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let checkboxIndicatorWidthHeight: CGFloat = 18
        let radioIndicatorWidthHeight: CGFloat = 20
    }
}

struct LegacyStepQuizTableSelectColumnsColumnView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool

    let text: String

    var isMultipleChoice: Bool

    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: appearance.interItemSpacing) {
                buildIndicator(isSelected: isSelected, onTap: onTap)

                LatexView(
                    text: text,
                    configuration: .quizContent()
                )
            }
        }
    }

    @ViewBuilder
    private func buildIndicator(isSelected: Bool, onTap: @escaping () -> Void) -> some View {
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

#if DEBUG
#Preview {
    VStack {
        LegacyStepQuizTableSelectColumnsColumnView(
            isSelected: true,
            text: "Some option",
            isMultipleChoice: false,
            onTap: {}
        )
        LegacyStepQuizTableSelectColumnsColumnView(
            isSelected: true,
            text: "Some option",
            isMultipleChoice: true,
            onTap: {}
        )
    }
    .padding()
}
#endif
