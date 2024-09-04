import shared
import SwiftUI

enum StepQuizCodeBlanksAppearance {
    static let activeBorderColor = Color(ColorPalette.primary)
    static let cornerRadius: CGFloat = 8

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

                StepQuizCodeBlanksCodeBlocksView(
                    state: contentState,
                    onCodeBlockTap: viewModel.doCodeBlockMainAction(_:),
                    onCodeBlockChildTap: viewModel.doCodeBlockChildMainAction(codeBlock:codeBlockChild:),
                    onSpaceTap: viewModel.doSpaceAction,
                    onDeleteTap: viewModel.doDeleteAction,
                    onEnterTap: viewModel.doEnterAction
                )
                .equatable()
                Divider()

                StepQuizCodeBlanksSuggestionsView(
                    suggestions: contentState.suggestions,
                    isAnimationEffectActive: contentState.isSuggestionsHighlightEffectActive,
                    onSuggestionTap: viewModel.doSuggestionMainAction(_:)
                )
                .equatable()
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
                    codeBlocks: [
                        StepQuizCodeBlanksViewStateCodeBlockItemBlank(id: 0, indentLevel: 0, isActive: true)
                    ],
                    suggestions: [Suggestion.Print()],
                    isDeleteButtonEnabled: true,
                    isSpaceButtonHidden: true,
                    onboardingState: StepQuizCodeBlanksFeatureOnboardingStateUnavailable()
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
                            indentLevel: 0,
                            children: [
                                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
                            ]
                        )
                    ],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonEnabled: false,
                    isSpaceButtonHidden: true,
                    onboardingState: StepQuizCodeBlanksFeatureOnboardingStateUnavailable()
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
                            indentLevel: 0,
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
                            indentLevel: 0,
                            children: [
                                StepQuizCodeBlanksViewStateCodeBlockChildItem(id: 0, isActive: true, value: nil)
                            ]
                        )
                    ],
                    suggestions: [
                        Suggestion.ConstantString(text: "There is a cat on the keyboard, it is true"),
                        Suggestion.ConstantString(text: "Typing messages out of the blue")
                    ],
                    isDeleteButtonEnabled: false,
                    isSpaceButtonHidden: true,
                    onboardingState: StepQuizCodeBlanksFeatureOnboardingStateUnavailable()
                )
            ),
            viewModel: StepQuizCodeBlanksViewModel()
        )

        Spacer()
    }
    .padding()
}
#endif
