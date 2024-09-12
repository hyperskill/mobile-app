import shared
import SwiftUI

struct StepQuizCodeBlanksPrintInstructionView: View {
    let printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint

    let onChildTap: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                Text("print(")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                ForEach(printItem.children, id: \.id) { child in
                    StepQuizCodeBlanksCodeBlockChildView(child: child, action: onChildTap)
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
                indentLevel: 0,
                children: [.init(id: 0, isActive: false, value: "")]
            ),
            onChildTap: { _ in }
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                indentLevel: 0,
                children: [.init(id: 0, isActive: true, value: "")]
            ),
            onChildTap: { _ in }
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                indentLevel: 0,
                children: [.init(id: 0, isActive: true, value: "There is a cat on the keyboard, it is true")]
            ),
            onChildTap: { _ in }
        )
        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                indentLevel: 0,
                children: [.init(id: 0, isActive: false, value: "There is a cat on the keyboard, it is true")]
            ),
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksPrintInstructionView(
            printItem: StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                id: 0,
                indentLevel: 0,
                children: [
                    .init(id: 0, isActive: false, value: "x"),
                    .init(id: 1, isActive: true, value: "")
                ]
            ),
            onChildTap: { _ in }
        )
    }
    .frame(maxWidth: .infinity)
    .padding()
}
#endif
