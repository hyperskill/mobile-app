import Combine
import Foundation
import shared

final class StepQuizStringViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let dataset: Dataset
    private let reply: Reply?

    private let dataType: StepQuizStringDataType

    @Published var viewData: StepQuizStringViewData
    private var viewDataDidChangeSubscription: AnyCancellable?

    init(dataType: StepQuizStringDataType, dataset: Dataset, reply: Reply?) {
        self.dataType = dataType
        self.dataset = dataset
        self.reply = reply
        self.viewData = StepQuizStringViewDataMapper.map(dataType: dataType, reply: reply)

        self.viewDataDidChangeSubscription = self.$viewData.sink { [weak self] newViewData in
            guard let strongSelf = self,
                  strongSelf.viewData.text != newViewData.text else {
                return
            }

            DispatchQueue.main.async {
                strongSelf.outputCurrentReply()
            }
        }
    }

    func doForceScoreCheckboxMainAction() {
        viewData.isForceScoreCheckboxChecked.toggle()
    }

    func createReply() -> Reply {
        switch self.dataType {
        case .string:
            Reply(text: viewData.text, files: [])
        case .number:
            Reply(number: viewData.text)
        case .math:
            Reply(formula: viewData.text)
        case .prompt:
            Reply.companion.prompt(
                prompt: viewData.text,
                markedAsCorrect: viewData.isForceScoreCheckboxChecked
            )
        }
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
