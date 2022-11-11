import CombineSchedulers
import Foundation

final class StepQuizCodeFullScreenViewModel: ObservableObject {
    weak var moduleOutput: StepQuizCodeFullScreenOutputProtocol?
    private let provideModuleInputCallback: (StepQuizCodeFullScreenInputProtocol?) -> Void

    @Published var codeQuizViewData: StepQuizCodeViewData

    private let mainScheduler: AnySchedulerOf<RunLoop>

    init(
        codeQuizViewData: StepQuizCodeViewData,
        provideModuleInputCallback: @escaping (StepQuizCodeFullScreenInputProtocol?) -> Void,
        mainScheduler: AnySchedulerOf<RunLoop> = .main
    ) {
        self.codeQuizViewData = codeQuizViewData
        self.provideModuleInputCallback = provideModuleInputCallback
        self.mainScheduler = mainScheduler
    }

    func doCodeUpdate(code: String?) {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.moduleOutput?.handleStepQuizCodeFullScreenUpdatedCode(code)
        }
    }

    func doRetry() {
        moduleOutput?.handleStepQuizCodeFullScreenRetryRequested()
    }

    func doRunCode() {
        moduleOutput?.handleStepQuizCodeFullScreenSubmitRequested()
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
    }
}

// MARK: - StepQuizCodeFullScreenViewModel: StepQuizCodeFullScreenInputProtocol -

extension StepQuizCodeFullScreenViewModel: StepQuizCodeFullScreenInputProtocol {
    func update(codeQuizViewData: StepQuizCodeViewData) {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.codeQuizViewData = codeQuizViewData
        }
    }
}
