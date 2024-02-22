import UIKit

protocol CodeEditorSuggestionsPresentationContextProviding: AnyObject {
    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController?
}

final class ResponderChainCodeEditorSuggestionsPresentationContextProvider:
    CodeEditorSuggestionsPresentationContextProviding {
    private weak var presentationController: UIViewController?

    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController? {
        if let presentationController {
            return presentationController
        } else {
            let responsibleViewController = codeEditorView.findViewController()
            presentationController = responsibleViewController
            return responsibleViewController
        }
    }
}
