import SwiftUI

extension StepQuizCodeFullScreenCodeView {
    struct Appearance {
        let codeEditorTextInsets: UIEdgeInsets

        init(codeElementsSize: CodeQuizElementsSize = DeviceInfo.current.isPad ? .big : .small) {
            self.codeEditorTextInsets = LayoutInsets(
                top: LayoutInsets.defaultInset,
                bottom: codeElementsSize.elements.toolbar.realSizes.viewHeight
            ).uiEdgeInsets
        }
    }
}
struct StepQuizCodeFullScreenCodeView: View {
    private(set) var appearance = Appearance()

    @Binding var code: String?

    let codeTemplate: String?

    let language: CodeLanguage?

    let isActionButtonsVisible: Bool

    let onDidBeginEditingCode: () -> Void
    let onDidEndEditingCode: () -> Void

    let onTapRetry: () -> Void
    let onTapRunCode: () -> Void

    let onDidTapInputAccessoryButton: (String) -> Void

    var body: some View {
        ZStack(alignment: .bottom) {
            CodeEditor(
                code: $code,
                codeTemplate: codeTemplate,
                language: language,
                textInsets: appearance.codeEditorTextInsets,
                onDidBeginEditing: onDidBeginEditingCode,
                onDidEndEditing: onDidEndEditingCode,
                onDidTapInputAccessoryButton: onDidTapInputAccessoryButton
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            if isActionButtonsVisible {
                StepQuizActionButtons(
                    retryButton: .init(action: onTapRetry),
                    primaryButton: .init(
                        state: .normal,
                        titleForState: StepQuizActionButtonCodeQuizDelegate.getTitle(for:),
                        systemImageNameForState: StepQuizActionButtonCodeQuizDelegate.getSystemImageName(for:),
                        action: onTapRunCode
                    )
                )
                .padding()
            }
        }
    }
}

#Preview {
    StepQuizCodeFullScreenCodeView(
        code: .constant("fun main() {\n    // put your code here\n}"),
        codeTemplate: "fun main() {\n    // put your code here\n}",
        language: .kotlin,
        isActionButtonsVisible: true,
        onDidBeginEditingCode: {},
        onDidEndEditingCode: {},
        onTapRetry: {},
        onTapRunCode: {},
        onDidTapInputAccessoryButton: { _ in }
    )
}
