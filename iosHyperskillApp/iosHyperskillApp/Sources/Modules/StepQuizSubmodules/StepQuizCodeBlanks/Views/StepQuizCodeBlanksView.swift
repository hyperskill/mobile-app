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

    var body: some View {
        if case .content(let contentState) = viewStateKs {
            VStack(alignment: .leading, spacing: 0) {
                Divider()
                titleView
                Divider()

                codeBlocksView(
                    codeBlocks: contentState.codeBlocks,
                    isDeleteButtonVisible: contentState.isDeleteButtonVisible
                )
                Divider()

                suggestionsView(
                    suggestions: contentState.suggestions
                )
                Divider()
            }
            .padding(.horizontal, -LayoutInsets.defaultInset)
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

    private func codeBlocksView(
        codeBlocks: [StepQuizCodeBlanksViewStateCodeBlockItem],
        isDeleteButtonVisible: Bool
    ) -> some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(codeBlocks, id: \.self) { codeBlock in
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
                }
            }

            HStack {
                Spacer()
                Button(
                    action: viewModel.doDeleteMainAction,
                    label: {
                        Image(systemName: "delete.left")
                            .imageScale(.large)
                            .padding(.vertical, LayoutInsets.smallInset / 2)
                            .padding(.horizontal, LayoutInsets.smallInset)
                            .background(Color(ColorPalette.primary))
                            .foregroundColor(Color(ColorPalette.onPrimary))
                            .cornerRadius(8)
                    }
                )
                .buttonStyle(BounceButtonStyle())
                .disabled(!isDeleteButtonVisible)
            }
            .padding(.horizontal)
            .conditionalOpacity(isEnabled: isDeleteButtonVisible, opacityDisabled: 0)
        }
        .padding(.vertical, LayoutInsets.defaultInset)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(BackgroundView())
    }

    private func suggestionsView(suggestions: [Suggestion]) -> some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(suggestions, id: \.self) { suggestion in
                Button(
                    action: {
                        viewModel.doSuggestionMainAction(suggestion)
                    },
                    label: {
                        StepQuizCodeBlanksOptionView(text: suggestion.text, isActive: true)
                    }
                )
                .buttonStyle(BounceButtonStyle())
            }
        }
        .padding(LayoutInsets.defaultInset)
        .frame(minHeight: 72)
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
                    codeBlocks: [StepQuizCodeBlanksViewStateCodeBlockItem.Blank(id: 0, isActive: true)],
                    suggestions: [Suggestion.Print()],
                    isDeleteButtonVisible: true
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
                    codeBlocks: [StepQuizCodeBlanksViewStateCodeBlockItem.Print(id: 0, isActive: true, output: nil)],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonVisible: false
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
                        StepQuizCodeBlanksViewStateCodeBlockItem.Print(
                            id: 0,
                            isActive: false,
                            output: "There is a cat on the keyboard, it is true"
                        ),
                        StepQuizCodeBlanksViewStateCodeBlockItem.Print(
                            id: 1,
                            isActive: true,
                            output: nil
                        )
                    ],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonVisible: false
                )
            ),
            viewModel: StepQuizCodeBlanksViewModel()
        )

        Spacer()
    }
    .padding()
}
#endif
