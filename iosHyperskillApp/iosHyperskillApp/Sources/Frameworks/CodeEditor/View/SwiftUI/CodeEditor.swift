import SwiftUI

struct CodeEditor: UIViewRepresentable {
    typealias UIViewType = CodeEditorView

    @Binding var code: String

    var codeTemplate: String?

    let language: CodeLanguage

    var theme: CodeEditorTheme?

    var shouldHighlightCurrentLine = false

    var textInsets = UIEdgeInsets.zero

    @Environment(\.isEnabled) private var isEnabled

    // MARK: UIViewRepresentable

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> CodeEditorView {
        let codeEditorView = CodeEditorView()
        codeEditorView.codeTemplate = codeTemplate
        codeEditorView.language = language
        codeEditorView.theme = theme
        codeEditorView.shouldHighlightCurrentLine = shouldHighlightCurrentLine
        codeEditorView.textInsets = textInsets

        codeEditorView.delegate = context.coordinator

        return codeEditorView
    }

    func updateUIView(_ codeEditorView: CodeEditorView, context: Context) {
        if codeEditorView.code != code {
            codeEditorView.code = code
        }
        if codeEditorView.isEditable != isEnabled {
            codeEditorView.isEditable = isEnabled
        }

        context.coordinator.onCodeDidChange = { newCode in
            self.code = newCode
        }
    }
}

// MARK: - CodeEditor (Coordinator) -

extension CodeEditor {
    class Coordinator: NSObject, CodeEditorViewDelegate {
        var onCodeDidChange: ((String) -> Void)?

        func codeEditorViewDidChange(_ codeEditorView: CodeEditorView) {
            onCodeDidChange?(codeEditorView.code ?? "")
        }

        func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool) {}

        func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView) {}

        func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView) {}

        func codeEditorViewDidRequestSuggestionPresentationController(
            _ codeEditorView: CodeEditorView
        ) -> UIViewController? {
            SourcelessRouter().currentPresentedViewController()
        }
    }
}

// MARK: - CodeEditor_Previews: PreviewProvider -

struct CodeEditor_Previews: PreviewProvider {
    static var previews: some View {
        CodeEditor(
            code: .constant(CodeLanguageSamples.sample(for: .java)),
            language: .java
        )
        .frame(height: 236)
        .frame(maxWidth: .infinity)
    }
}
