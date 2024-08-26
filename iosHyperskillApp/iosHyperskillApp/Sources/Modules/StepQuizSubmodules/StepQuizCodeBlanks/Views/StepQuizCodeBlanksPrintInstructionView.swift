import shared
import SwiftUI

struct StepQuizCodeBlanksPrintInstructionView: View {
    let printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                Text("print(")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                ForEach(printItem.children, id: \.id) { child in
                    if let value = child.value, !value.isEmpty {
                        StepQuizCodeBlanksOptionView(text: value, isActive: child.isActive)
                    } else {
                        StepQuizCodeBlanksBlankView(style: .small, isActive: child.isActive)
                    }
                }

                Text(")")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)
            }
            .padding(.horizontal, LayoutInsets.defaultInset)
            .padding(.vertical, LayoutInsets.smallInset)
            .background(Color(ColorPalette.violet400Alpha7))
            .cornerRadius(StepQuizCodeBlanksAppearance.cornerRadius)
            .padding(.horizontal)
        }
        .scrollBounceBehaviorBasedOnSize(axes: .horizontal)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                children: [.init(id: 0, isActive: false, value: "")]
            )
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                children: [.init(id: 0, isActive: true, value: "")]
            )
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                children: [.init(id: 0, isActive: true, value: "There is a cat on the keyboard, it is true")]
            )
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                children: [.init(id: 0, isActive: false, value: "There is a cat on the keyboard, it is true")]
            )
        )

        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                children: [
                    .init(id: 0, isActive: false, value: "x"),
                    .init(id: 1, isActive: true, value: "")
                ]
            )
        )
    }
    .frame(maxWidth: .infinity)
    .padding()
}
#endif
