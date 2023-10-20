import Combine
import Foundation
import shared

final class StepQuizFillBlanksViewModel: ObservableObject {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?
    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    @Published private(set) var viewData: StepQuizFillBlanksViewData

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        viewDataMapper: StepQuizFillBlanksViewDataMapper,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.provideModuleInputCallback = provideModuleInputCallback
        self.viewData = viewDataMapper.mapToViewData(dataset: dataset, reply: reply)
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
    }
}

// MARK: - StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol -

extension StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol {
    func createReply() -> Reply {
        let blanks: [String] = viewData.components.compactMap { component in
            switch component.type {
            case .text:
                return nil
            case .input:
                return component.text ?? ""
            }
        }

        return Reply.companion.fillBlanks(blanks: blanks)
    }
}
