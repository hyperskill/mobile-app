import shared
import SwiftUI

enum StepQuizCodeBlanksAppearance {
    static let activeBorderColor = Color(ColorPalette.primary)

    static let blankTextColor = Color.primaryText
    static let blankFont = Font(CodeEditorThemeService().theme.font)
}

struct StepQuizCodeBlanksView: View {
    let viewStateKs: StepQuizCodeBlanksViewStateKs

    let viewModel: StepQuizCodeBlanksViewModel

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        if case .content(let contentState) = viewStateKs {
            VStack(alignment: .leading, spacing: 0) {
                Divider()
                titleView
                Divider()

                codeBlocksView(
                    codeBlocks: contentState.codeBlocks,
                    isDeleteButtonEnabled: contentState.isDeleteButtonEnabled
                )
                Divider()

                StepQuizCodeBlanksSuggestionsView(
                    suggestions: contentState.suggestions,
                    onSuggestionTap: viewModel.doSuggestionMainAction(_:)
                )
                Divider()
            }
            .padding(.horizontal, -LayoutInsets.defaultInset)
            .conditionalOpacity(isEnabled: isEnabled)
        }
    }

    private var titleView: some View {
        Text(Strings.StepQuizCode.title)
            .font(.headline)
            .foregroundColor(.primaryText)
            .padding(.horizontal)
            .padding(.vertical, LayoutInsets.smallInset)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(BackgroundView())
    }

    @MainActor
    private func codeBlocksView(
        codeBlocks: [StepQuizCodeBlanksViewStateCodeBlockItem],
        isDeleteButtonEnabled: Bool
    ) -> some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(codeBlocks, id: \.id_) { codeBlock in
                switch StepQuizCodeBlanksViewStateCodeBlockItemKs(codeBlock) {
                case .blank(let blankItem):
                    StepQuizCodeBlanksBlankView(
                        style: .large,
                        isActive: blankItem.isActive
                    )
                    .padding(.horizontal)
                    .onTapGesture {
                        viewModel.doCodeBlockMainAction(codeBlock)
                    }
                case .print(let printItem):
                    StepQuizCodeBlanksPrintInstructionView(
                        isActive: printItem.isActive,
                        output: printItem.output
                    )
                    .onTapGesture {
                        viewModel.doCodeBlockMainAction(codeBlock)
                    }
                case .variable(let variableItem):
                    StepQuizCodeBlanksVariableInstructionView(
                        variableItem: variableItem,
                        onChildTap: { codeBlockChild in
                            viewModel.doCodeBlockChildMainAction(
                                codeBlock: codeBlock,
                                codeBlockChild: codeBlockChild
                            )
                        }
                    )
                    .onTapGesture {
                        viewModel.doCodeBlockMainAction(codeBlock)
                    }
                }
            }

            HStack(spacing: LayoutInsets.defaultInset) {
                Spacer()

                StepQuizCodeBlanksActionButton
                    .delete(action: viewModel.doDeleteAction)
                    .disabled(!isDeleteButtonEnabled)

                StepQuizCodeBlanksActionButton
                    .enter(action: viewModel.doEnterAction)
            }
            .padding(.horizontal)
        }
        .padding(.vertical, LayoutInsets.defaultInset)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(BackgroundView())
    }
}

extension StepQuizCodeBlanksView: Equatable {
    static func == (lhs: StepQuizCodeBlanksView, rhs: StepQuizCodeBlanksView) -> Bool {
        lhs.viewStateKs == rhs.viewStateKs
    }
}

#if DEBUG
#Preview("Blank") {
    VStack {
        StepQuizCodeBlanksView(
            viewStateKs: .content(
                StepQuizCodeBlanksViewStateContent(
                    codeBlocks: [StepQuizCodeBlanksViewStateCodeBlockItemBlank(id: 0, isActive: true)],
                    suggestions: [Suggestion.Print()],
                    isDeleteButtonEnabled: true
                )
            ),
            viewModel: StepQuizCodeBlanksViewModel()
        )

        Spacer()
    }
    .padding()
}

#Preview("Not filled Print") {
    VStack {
        StepQuizCodeBlanksView(
            viewStateKs: .content(
                StepQuizCodeBlanksViewStateContent(
                    codeBlocks: [
                        StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                            id: 0,
                            children: [
                                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
                            ]
                        )
                    ],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonEnabled: false
                )
            ),
            viewModel: StepQuizCodeBlanksViewModel()
        )

        Spacer()
    }
    .padding()
}

#Preview("Filled Print and not filled one") {
    VStack {
        StepQuizCodeBlanksView(
            viewStateKs: .content(
                StepQuizCodeBlanksViewStateContent(
                    codeBlocks: [
                        StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                            id: 0,
                            children: [
                                StepQuizCodeBlanksViewStateCodeBlockChildItem(
                                    id: 0,
                                    isActive: false,
                                    value: "There is a cat on the keyboard, it is true"
                                )
                            ]
                        ),
                        StepQuizCodeBlanksViewStateCodeBlockItemPrint(
                            id: 1,
                            children: [
                                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
                            ]
                        )
                    ],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonEnabled: false
                )
            ),
            viewModel: StepQuizCodeBlanksViewModel()
        )

        Spacer()
    }
    .padding()
}
#endif
