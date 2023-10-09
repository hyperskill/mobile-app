import UIKit

protocol CodeEditorViewDelegate: AnyObject {
    func codeEditorViewDidChange(_ codeEditorView: CodeEditorView)
    func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool)
    func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView)
    func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView)
    func codeEditorViewDidRequestSuggestionPresentationController(_ codeEditorView: CodeEditorView) -> UIViewController?
    func codeEditorViewDidChangeHeight(_ codeEditorView: CodeEditorView, height: CGFloat)

    func codeEditorViewDidTapTabInputAccessoryButton(_ codeEditorView: CodeEditorView)
    func codeEditorViewDidTapHideKeyboardInputAccessoryButton(_ codeEditorView: CodeEditorView)
    func codeEditorView(_ codeEditorView: CodeEditorView, didTapInputAccessoryButton symbol: String)
}

extension CodeEditorViewDelegate {
    func codeEditorViewDidChange(_ codeEditorView: CodeEditorView) {}

    func codeEditorView(_ codeEditorView: CodeEditorView, beginEditing editing: Bool) {}

    func codeEditorViewDidBeginEditing(_ codeEditorView: CodeEditorView) {}

    func codeEditorViewDidEndEditing(_ codeEditorView: CodeEditorView) {}

    func codeEditorViewDidRequestSuggestionPresentationController(
        _ codeEditorView: CodeEditorView
    ) -> UIViewController? {
        nil
    }

    func codeEditorViewDidTapTabInputAccessoryButton(_ codeEditorView: CodeEditorView) {}

    func codeEditorViewDidTapHideKeyboardInputAccessoryButton(_ codeEditorView: CodeEditorView) {}

    func codeEditorView(_ codeEditorView: CodeEditorView, didTapInputAccessoryButton symbol: String) {}
}
