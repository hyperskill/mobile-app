import UIKit

protocol CodeEditorSuggestionsPresentationContextProviding: AnyObject {
    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController?
}

final class CodeEditorSuggestionsPresentationContextProvider: CodeEditorSuggestionsPresentationContextProviding {
    weak var presentationController: UIViewController?

    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController? { presentationController }
}
