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

        let text: String = { () -> String? in
            guard let reply = reply else {
                return nil
            }

            switch dataType {
            case .string:
                return reply.text
            case .number:
                return reply.number
            case .math:
                return reply.formula
            }
        }() ?? ""
        self.viewData = StepQuizStringViewData(
            text: text,
            placeholder: Strings.StepQuizString.placeholder,
            isDecimalTextInput: dataType == .number
        )

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

    func createReply() -> Reply {
        switch self.dataType {
        case .string:
            return Reply(text: viewData.text, files: [])
        case .number:
            return Reply(number: viewData.text)
        case .math:
            return Reply(formula: viewData.text)
        }
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
