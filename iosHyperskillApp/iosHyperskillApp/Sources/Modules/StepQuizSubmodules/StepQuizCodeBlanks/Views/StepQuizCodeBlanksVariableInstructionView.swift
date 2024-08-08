import shared
import SwiftUI

struct StepQuizCodeBlanksVariableInstructionView: View {
    let variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable

    let onChildTap: (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                if let nameChild = variableItem.name {
                    childView(child: nameChild)
                        .onTapGesture {
                            onChildTap(nameChild)
                        }
                }

                Text("=")
                    .font(StepQuizCodeBlanksAppearance.blankFont)
                    .foregroundColor(StepQuizCodeBlanksAppearance.blankTextColor)

                if let valueChild = variableItem.value {
                    childView(child: valueChild)
                        .onTapGesture {
                            onChildTap(valueChild)
                        }
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

    @ViewBuilder
    private func childView(
        child: StepQuizCodeBlanksViewStateCodeBlockChildItem
    ) -> some View {
        if let value = child.value {
            StepQuizCodeBlanksOptionView(text: value, isActive: child.isActive)
        } else {
            StepQuizCodeBlanksBlankView(style: .small, isActive: child.isActive)
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksVariableInstructionView(
            variableItem: StepQuizCodeBlanksViewStateCodeBlockItemVariable(
                id: 0,
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
