import SwiftUI

struct CodeEditor: UIViewRepresentable {
    typealias UIViewType = CodeEditorView

    @Binding var code: String?

    var codeTemplate: String?

    let language: CodeLanguage?

    var theme: CodeEditorTheme?

    var isEditable = true

    var textInsets = UIEdgeInsets.zero

    var appearance: CodeEditorView.Appearance?

    var themeService: CodeEditorThemeServiceProtocol = CodeEditorThemeService()

    var elementsSize: CodeQuizElementsSize = DeviceInfo.current.isPad ? .big : .small

    // MARK: UIViewRepresentable

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> CodeEditorView {
        let appearance = self.appearance ?? .makeDefault(elementsSize: elementsSize)
        let codeEditorView = CodeEditorView(
            appearance: appearance,
            codeEditorThemeService: themeService,
            elementsSize: elementsSize
        )
        codeEditorView.codeTemplate = codeTemplate
        codeEditorView.language = language
        codeEditorView.theme = theme ?? themeService.theme
        codeEditorView.shouldHighlightCurrentLine = false
        codeEditorView.textInsets = textInsets

        codeEditorView.delegate = context.coordinator

        return codeEditorView
    }

    func updateUIView(_ codeEditorView: CodeEditorView, context: Context) {
        if codeEditorView.code != code {
            codeEditorView.code = code
        }
        if codeEditorView.isEditable != isEditable {
            codeEditorView.isEditable = isEditable
        }

        context.coordinator.onCodeDidChange = { newCode in
            self.code = newCode
        }
        context.coordinator.onDidBeginEditing = { [weak codeEditorView] in
            guard let codeEditorView = codeEditorView else {
                return
            }

            codeEditorView.shouldHighlightCurrentLine = true
        }
        context.coordinator.onDidEndEditing = { [weak codeEditorView] in
            guard let codeEditorView = codeEditorView else {
                return
            }

            codeEditorView.shouldHighlightCurrentLine = false
        }
    }
}

// MARK: - CodeEditor (Coordinator) -

extension CodeEditor {
    class Coordinator: NSObject, CodeEditorViewDelegate {
        var onCodeDidChange: ((String) -> Void)?

        var onDidBeginEditing: (() -> Void)?

        var onDidEndEditing: (() -> Void)?

        func codeEditorViewDidChange(_ codeEditorView: CodeEditorView) {
            onCodeDidChange?(codeEditorView.code ?? "")
        }

        func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool) {}

        func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView) {
            onDidBeginEditing?()
        }

        func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView) {
            onDidEndEditing?()
        }

        func codeEditorViewDidRequestSuggestionPresentationController(
            _ codeEditorView: CodeEditorView
        ) -> UIViewController? {
            // TODO: Walk up and find correct controller otherwise UIViewControllerHierarchyInconsistency will be thrown
//            guard let rootViewController = SourcelessRouter().window?.rootViewController else {
//                return nil
//            }
//
//            return rootViewController.children.first?.children.first?.children.first?.children.first
            nil
        }
    }
}

// MARK: - CodeEditorView.Appearance (Default) -

private extension CodeEditorView.Appearance {
    static func makeDefault(elementsSize: CodeQuizElementsSize) -> CodeEditorView.Appearance {
        CodeEditorView.Appearance(
            textViewAppearance: .init(
                gutterWidth: elementsSize.elements.editor.realSizes.gutterWidth,
                gutterBorderWidth: 0.5,
                lineNumberFont: UIFont.monospacedDigitSystemFont(
                    ofSize: elementsSize.elements.editor.realSizes.lineNumberFontSize,
                    weight: .regular
                ),
                lineNumberInsets: LayoutInsets(trailing: 4),
                lineSpacing: 1.2,
                currentLineWidth: elementsSize.elements.editor.realSizes.gutterWidth,
                colorsUpdateStrategy: .invertThemeBackgroundColor(
                    alphaComponents: .init(
                        gutterBorderColorAlpha: 0.09,
                        lineNumberTextColorAlpha: 0.38,
                        currentLineColorAlpha: 0.09,
                        currentLineNumberTextColorAlpha: 0.6
                    )
                )
            )
        )
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
