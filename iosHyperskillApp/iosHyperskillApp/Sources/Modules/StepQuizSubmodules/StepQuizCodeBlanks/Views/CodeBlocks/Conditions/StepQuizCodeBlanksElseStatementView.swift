import shared
import SwiftUI

struct StepQuizCodeBlanksElseStatementView: View {
    let elseStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemElseStatement

    var body: some View {
        Text("else:")
            .font(StepQuizCodeBlanksAppearance.blankFont)
            .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)
            .padding(.horizontal, LayoutInsets.defaultInset)
            .padding(.vertical, LayoutInsets.smallInset)
            .frame(minHeight: StepQuizCodeBlanksCodeBlockChildTextView.Appearance.minHeight)
            .background(Color(ColorPalette.violet400Alpha7))
            .addBorder(
                color: elseStatementItem.isActive ? StepQuizCodeBlanksAppearance.activeBorderColor : .clear,
                width: elseStatementItem.isActive ? 1 : 0,
                cornerRadius: StepQuizCodeBlanksAppearance.cornerRadius
            )
            .padding(.horizontal)
            .animation(.default, value: elseStatementItem.isActive)
    }
}

#if DEBUG
#Preview {
    StepQuizCodeBlanksElseStatementView(
        elseStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemElseStatement(
            id: 0,
            indentLevel: 0,
            isActive: true
        )
    )
}

#Preview {
    StepQuizCodeBlanksElseStatementView(
        elseStatementItem: StepQuizCodeBlanksViewStateCodeBlockItemElseStatement(
            id: 0,
            indentLevel: 0,
            isActive: false
        )
    )
}
#endif
