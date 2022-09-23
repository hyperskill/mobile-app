import UIKit

protocol CodeEditorSuggestionsPresentationContextProviding: AnyObject {
    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController?
}

// swiftlint:disable all

final class ResponderChainCodeEditorSuggestionsPresentationContextProvider:
  CodeEditorSuggestionsPresentationContextProviding
{
    private weak var presentationController: UIViewController?

    func presentationController(for codeEditorView: CodeEditorView) -> UIViewController? {
        if let presentationController = presentationController {
            return presentationController
        } else {
            let responsibleViewController = codeEditorView.findViewController()
            presentationController = responsibleViewController
            return responsibleViewController
        }
    }
}
