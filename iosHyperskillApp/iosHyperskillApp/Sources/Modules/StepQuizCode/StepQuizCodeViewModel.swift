import Combine
import Foundation
import shared

final class StepQuizCodeViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    private let viewDataMapper: StepQuizCodeViewDataMapper

    @Published private(set) var viewData: StepQuizCodeViewData

    @Published var navigationState = StepQuizCodeNavigationState()

    init(step: Step, dataset: Dataset, reply: Reply?, viewDataMapper: StepQuizCodeViewDataMapper) {
        self.step = step
        self.dataset = dataset
        self.reply = reply

        self.viewDataMapper = viewDataMapper
        self.viewData = self.viewDataMapper.mapCodeDataToViewData(step: self.step, reply: self.reply)
    }

    func createReply() -> Reply {
        Reply(language: viewData.languageStringValue, code: viewData.code)
    }

    private func syncReply(code: String?) {
        let reply = Reply(language: viewData.languageStringValue, code: code)
        moduleOutput?.handleChildQuizSync(reply: reply)
    }

    func logClickedCodeDetailsEvent() {
        moduleOutput?.handleChildQuizAnalyticEventMessage(StepQuizFeatureMessageClickedCodeDetailsEventMessage())
    }
}

// MARK: - StepQuizCodeViewModel: StepQuizCodeFullScreenOutputProtocol -

extension StepQuizCodeViewModel: StepQuizCodeFullScreenOutputProtocol {
    func handleStepQuizCodeFullScreenUpdatedCode(_ code: String?) {
        viewData.code = code

        DispatchQueue.main.async {
            self.syncReply(code: code)
        }
    }

    func handleStepQuizCodeFullScreenRetryRequested() {
        DispatchQueue.main.async {
            self.moduleOutput?.handleChildQuizRetry()
        }
    }

    func handleStepQuizCodeFullScreenSubmitRequested() {
        navigationState.presentingFullScreen = false

        DispatchQueue.main.async {
            self.moduleOutput?.handleChildQuizSubmitCurrentReply()
        }
    }
}
