import Foundation

protocol StepQuizCodeFullScreenOutputProtocol: AnyObject {
    func handleStepQuizCodeFullScreenUpdatedCode(_ code: String?)
    func handleStepQuizCodeFullScreenRetryRequested()
    func handleStepQuizCodeFullScreenSubmitRequested()
    // Analytic
    func handleStepQuizCodeFullScreenToggledStepTextDetails()
    func handleStepQuizCodeFullScreenToggledCodeDetails()
    func handleStepQuizCodeFullScreenTappedInputAccessoryButton(symbol: String)
}
