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
    let onDecreaseIndentLevelTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(state.codeBlocks, id: \.id_) { codeBlock in
                HStack(spacing: 0) {
                    Spacer()
                        .frame(width: LayoutInsets.defaultInset * CGFloat(codeBlock.indentLevel))

                    buildCodeBlockView(
                        codeBlock: codeBlock,
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
                    isDecreaseIndentLevelButtonHidden: state.isDecreaseIndentLevelButtonHidden,
                    onSpaceTap: onSpaceTap,
                    onDeleteTap: onDeleteTap,
                    onEnterTap: onEnterTap,
                    onDecreaseIndentLevelTap: onDecreaseIndentLevelTap
                )
            }
        }
        .padding(.vertical, LayoutInsets.defaultInset)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(BackgroundView())
    }

    @ViewBuilder
    private func buildCodeBlockView(
        codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem,
        onChildTap: @escaping (StepQuizCodeBlanksViewStateCodeBlockChildItem) -> Void
    ) -> some View {
        switch StepQuizCodeBlanksViewStateCodeBlockItemKs(codeBlock) {
        case .blank(let blankItem):
            StepQuizCodeBlanksCodeBlockChildBlankView(
                style: .large,
                isActive: blankItem.isActive
            )
            .padding(.horizontal)
        case .print(let printItem):
            StepQuizCodeBlanksPrintInstructionView(
                printItem: printItem,
                onChildTap: onChildTap
            )
        case .variable(let variableItem):
            StepQuizCodeBlanksVariableInstructionView(
                variableItem: variableItem,
                onChildTap: onChildTap
            )
        case .ifStatement(let ifStatementItem):
            StepQuizCodeBlanksIfStatementView(
                ifStatementItem: ifStatementItem,
                onChildTap: onChildTap
            )
        case .elifStatement(let elifStatementItem):
            StepQuizCodeBlanksElifStatementView(
                elifStatementItem: elifStatementItem,
                onChildTap: onChildTap
            )
        case .elseStatement(let elseStatementItem):
            StepQuizCodeBlanksElseStatementView(
                elseStatementItem: elseStatementItem
            )
        }
    }
}

extension StepQuizCodeBlanksCodeBlocksView: Equatable {
    static func == (lhs: StepQuizCodeBlanksCodeBlocksView, rhs: StepQuizCodeBlanksCodeBlocksView) -> Bool {
        lhs.state.isEqual(rhs)
    }
}
