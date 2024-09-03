import shared
import SwiftUI

struct StepQuizCodeBlanksCodeBlocksView: View {
    let state: StepQuizCodeBlanksViewStateContent

    let onCodeBlockTap: (StepQuizCodeBlanksViewStateCodeBlockItem) -> Void
    let onCodeBlockChildTap: (
        StepQuizCodeBlanksViewStateCodeBlockItem,
        StepQuizCodeBlanksViewStateCodeBlockChildItem
    ) -> Void

    let onSpaceTap: () -> Void
    let onDeleteTap: () -> Void
    let onEnterTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(state.codeBlocks, id: \.id_) { codeBlock in
                switch StepQuizCodeBlanksViewStateCodeBlockItemKs(codeBlock) {
                case .blank(let blankItem):
                    StepQuizCodeBlanksCodeBlockChildBlankView(
                        style: .large,
                        isActive: blankItem.isActive
                    )
                    .padding(.horizontal)
                    .onTapGesture {
                        onCodeBlockTap(codeBlock)
                    }
                case .print(let printItem):
                    StepQuizCodeBlanksPrintInstructionView(
                        printItem: printItem,
                        onChildTap: { codeBlockChild in
                            onCodeBlockChildTap(codeBlock, codeBlockChild)
                        }
                    )
                    .onTapGesture {
                        onCodeBlockTap(codeBlock)
                    }
                case .variable(let variableItem):
                    StepQuizCodeBlanksVariableInstructionView(
                        variableItem: variableItem,
                        onChildTap: { codeBlockChild in
                            onCodeBlockChildTap(codeBlock, codeBlockChild)
                        }
                    )
                    .onTapGesture {
                        onCodeBlockTap(codeBlock)
                    }
                }
            }

            if !state.isActionButtonsHidden {
                StepQuizCodeBlanksActionButtonsView(
                    isDeleteButtonEnabled: state.isDeleteButtonEnabled,
                    isSpaceButtonHidden: state.isSpaceButtonHidden,
                    onSpaceTap: onSpaceTap,
                    onDeleteTap: onDeleteTap,
                    onEnterTap: onEnterTap
                )
            }
        }
        .padding(.vertical, LayoutInsets.defaultInset)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(BackgroundView())
    }
}

extension StepQuizCodeBlanksCodeBlocksView: Equatable {
    static func == (lhs: StepQuizCodeBlanksCodeBlocksView, rhs: StepQuizCodeBlanksCodeBlocksView) -> Bool {
        lhs.state.isEqual(rhs)
    }
}
