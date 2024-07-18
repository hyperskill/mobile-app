import Foundation
import shared

final class StepQuizCodeBlanksViewModel {
    weak var moduleOutput: StepQuizCodeBlanksOutputProtocol?

    func doSuggestionMainAction(_ suggestion: Suggestion) {
        moduleOutput?.handleStepQuizCodeBlanksDidTapOnSuggestion(suggestion)
    }

    func doCodeBlockMainAction(_ codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem) {
        moduleOutput?.handleStepQuizCodeBlanksDidTapOnCodeBlock(codeBlock)
    }

    func doDeleteMainAction() {
        moduleOutput?.handleStepQuizCodeBlanksDidTapDelete()
    }
}
