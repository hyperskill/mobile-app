import Foundation
import shared

protocol StepQuizCodeBlanksOutputProtocol: AnyObject {
    func handleStepQuizCodeBlanksDidTapOnSuggestion(_ suggestion: Suggestion)
    func handleStepQuizCodeBlanksDidTapOnCodeBlock(_ codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem)
    func handleStepQuizCodeBlanksDidTapDelete()
    func handleStepQuizCodeBlanksDidTapEnter()
}
