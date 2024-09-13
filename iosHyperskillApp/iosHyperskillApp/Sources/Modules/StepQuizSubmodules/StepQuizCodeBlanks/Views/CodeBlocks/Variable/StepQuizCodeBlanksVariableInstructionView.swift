import shared
import SwiftUI

struct StepQuizCodeBlanksVariableInstructionView: View {
    let variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable

    let onChildTap: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                if let nameChild = variableItem.name {
                    StepQuizCodeBlanksCodeBlockChildView(child: nameChild, action: onChildTap)
                }

                Text("=")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                ForEach(variableItem.values, id: \.id) { child in
                    StepQuizCodeBlanksCodeBlockChildView(child: child, action: onChildTap)
                }
            }
            .padding(.horizontal, LayoutInsets.defaultInset)
            .padding(.vertical, LayoutInsets.smallInset)
            .background(Color(ColorPalette.violet400Alpha7))
            .cornerRadius(8)
            .animation(.default, value: variableItem)
            .padding(.horizontal)
        }
        .scrollBounceBehaviorBasedOnSize(axes: .horizontal)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksVariableInstructionView(
            variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable(
                id: 0,
                indentLevel: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil),
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 1, isActive: false, value: nil)
                ]
            ),
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksVariableInstructionView(
            variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable(
                id: 0,
                indentLevel: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: false, value: "fruit_a"),
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 1, isActive: true, value: nil)
                ]
            ),
            onChildTap: { _ in }
        )

        StepQuizCodeBlanksVariableInstructionView(
            variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable(
                id: 0,
                indentLevel: 0,
                children: [
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: false, value: "fruit_a"),
                    StepQuizCodeBlanksViewStateCodeBlockChildItem(
                        id: 1,
                        isActive: true,
                        value: "Typing messages out of the blue"
                    )
                ]
            ),
            onChildTap: { _ in }
        )
    }
    .frame(maxWidth: .infinity)
    .padding()
}
#endif
