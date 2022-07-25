import Foundation

final class StepQuizCodeFullScreenViewModel: ObservableObject {
    weak var moduleOutput: StepQuizCodeFullScreenOutputProtocol?

    let codeQuizViewData: StepQuizCodeViewData

    init(codeQuizViewData: StepQuizCodeViewData) {
        self.codeQuizViewData = codeQuizViewData
    }

    func doCodeUpdate(code: String?) {
        moduleOutput?.handleStepQuizCodeFullScreenUpdatedCode(code)
    }

    func doRetry() {
        moduleOutput?.handleStepQuizCodeFullScreenRetryRequested()
    }

    func doRunCode() {
        moduleOutput?.handleStepQuizCodeFullScreenSubmitRequested()
    }
}
