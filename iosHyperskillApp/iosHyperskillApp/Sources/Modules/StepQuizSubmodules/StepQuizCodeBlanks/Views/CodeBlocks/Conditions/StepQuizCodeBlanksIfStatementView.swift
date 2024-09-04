import shared
import SwiftUI

struct StepQuizCodeBlanksIfStatementView: View {
    let ifStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemIfStatement

    let onChildTap: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                Text("if")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                ForEach(ifStatementItem.children, id: \.id) { child in
                    StepQuizCodeBlanksCodeBlockChildView(child: child, action: onChildTap)
                }

                Text(":")
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
        StepQuizCodeBlanksIfStatementView(
            ifStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemIfStatement(
                id: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
                ]
            ),
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksIfStatementView(
            ifStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemIfStatement(
                id: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: "x")
                ]
            ),
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksIfStatementView(
            ifStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemIfStatement(
                id: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: false, value: "x"),
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 1, isActive: true, value: nil)
                ]
            ),
            onChildTap: { _ in }
        )
    }
    .padding()
}
#endif
