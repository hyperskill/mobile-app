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

    var onDidBeginEditingCode: () -> Void
    var onDidEndEditingCode: () -> Void

    var body: some View {
        ZStack(alignment: .center) {
            CodeEditor(
                code: $code,
                codeTemplate: codeTemplate,
                language: language,
                textInsets: appearance.codeEditorTextInsets,
                onDidBeginEditing: onDidBeginEditingCode,
                onDidEndEditing: onDidEndEditingCode
            )
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
    }
}

struct StepQuizCodeFullScreenCodeView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenCodeView(
            code: .constant("fun main() {\n    // put your code here\n}"),
            codeTemplate: "fun main() {\n    // put your code here\n}",
            language: .kotlin,
            onDidBeginEditingCode: {},
            onDidEndEditingCode: {}
        )
    }
}
