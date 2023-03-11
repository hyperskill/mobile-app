import Combine
import CombineSchedulers
import Foundation
import shared

class StepQuizCodeViewModel: ObservableObject {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    weak var fullScreenModuleInput: StepQuizCodeFullScreenInputProtocol?

    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    let step: Step
    private let dataset: Dataset
    private let reply: Reply?

    private let viewDataMapper: StepQuizCodeViewDataMapper
    @Published private(set) var viewData: StepQuizCodeViewData

    private let mainScheduler: AnySchedulerOf<RunLoop>

    @Published var navigationState = StepQuizCodeNavigationState()

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        viewDataMapper: StepQuizCodeViewDataMapper,
        mainScheduler: AnySchedulerOf<RunLoop> = .main,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.step = step
        self.dataset = dataset
        self.reply = reply

        self.viewDataMapper = viewDataMapper
        self.viewData = self.viewDataMapper.mapCodeDataToViewData(step: self.step, reply: self.reply)

        self.mainScheduler = mainScheduler
        self.provideModuleInputCallback = provideModuleInputCallback
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
    }

    func logClickedCodeDetailsEvent() {
        moduleOutput?.handleChildQuizAnalyticEventMessage(StepQuizFeatureMessageClickedCodeDetailsEventMessage())
    }
}

// MARK: - StepQuizCodeViewModel: StepQuizChildQuizInputProtocol -

extension StepQuizCodeViewModel: StepQuizChildQuizInputProtocol {
    @objc
    func createReply() -> Reply {
        Reply(language: viewData.languageStringValue, code: viewData.code)
    }

    func update(step: Step, dataset: Dataset, reply: Reply?) {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.viewData = strongSelf.viewDataMapper.mapCodeDataToViewData(step: step, reply: reply)
            strongSelf.updateFullScreenModule()
        }
    }

    // MARK: Private Helpers

    private func updateFullScreenModule() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.fullScreenModuleInput?.update(codeQuizViewData: strongSelf.viewData)
        }
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

    @objc
    func syncReply(code: String?) {
        let reply = Reply(language: viewData.languageStringValue, code: code)
        moduleOutput?.handleChildQuizSync(reply: reply)
    }
}
