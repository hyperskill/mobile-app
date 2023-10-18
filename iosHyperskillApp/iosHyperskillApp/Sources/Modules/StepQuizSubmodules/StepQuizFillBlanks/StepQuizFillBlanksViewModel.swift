import Combine
import Foundation
import shared

final class StepQuizFillBlanksViewModel: ObservableObject {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?
    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.provideModuleInputCallback = provideModuleInputCallback
    }
}

// MARK: - StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol -

extension StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol {
    func createReply() -> Reply {
        Reply.companion.fillBlanks(blanks: [])
    }
}
