import Foundation
import shared

final class StepQuizCodeBlanksViewModel {
    private let selectionFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)
    private let impactFeedbackGenerator = FeedbackGenerator(feedbackType: .impact(.soft))

    weak var moduleOutput: StepQuizCodeBlanksOutputProtocol?

    @MainActor
    func doSuggestionMainAction(_ suggestion: Suggestion) {
        selectionFeedbackGenerator.triggerFeedback()
        moduleOutput?.handleStepQuizCodeBlanksDidTapOnSuggestion(suggestion)
    }

    @MainActor
    func doCodeBlockMainAction(_ codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem) {
        selectionFeedbackGenerator.triggerFeedback()
        moduleOutput?.handleStepQuizCodeBlanksDidTapOnCodeBlock(codeBlock)
    }

    @MainActor
    func doDeleteAction() {
        impactFeedbackGenerator.triggerFeedback()
        moduleOutput?.handleStepQuizCodeBlanksDidTapDelete()
    }

    @MainActor
    func doEnterAction() {
        impactFeedbackGenerator.triggerFeedback()
        moduleOutput?.handleStepQuizCodeBlanksDidTapEnter()
    }
}
