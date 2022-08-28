import Combine
import Foundation
import shared

final class StepQuizCodeViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

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

    private func syncReply(code: String?) {
        let reply = Reply(language: viewData.languageStringValue, code: code)
        delegate?.handleChildQuizSync(reply: reply)
    }

    func logClickedCodeDetailsEvent() {
        delegate?.handleChildQuizAnalyticEventMessage(StepQuizFeatureMessageStepQuizClickedCodeDetailsEventMessage())
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
            self.delegate?.handleChildQuizRetry()
        }
    }

    func handleStepQuizCodeFullScreenSubmitRequested() {
        navigationState.presentingFullScreen = false

        DispatchQueue.main.async {
            self.delegate?.handleChildQuizSubmitCurrentReply()
        }
    }
}
