import Foundation
import shared

protocol StepQuizCodeBlanksOutputProtocol: AnyObject {
    func handleStepQuizCodeBlanksDidTapOnSuggestion(_ suggestion: Suggestion)
    func handleStepQuizCodeBlanksDidTapOnCodeBlock(_ codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem)
    func handleStepQuizCodeBlanksDidTapOnCodeBlockChild(
        codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem,
        codeBlockChild: StepQuizCodeBlanksViewStateCodeBlockChildItem
    )
    func handleStepQuizCodeBlanksDidTapDelete()
    func handleStepQuizCodeBlanksDidTapEnter()
    func handleStepQuizCodeBlanksDidTapSpace()
}
