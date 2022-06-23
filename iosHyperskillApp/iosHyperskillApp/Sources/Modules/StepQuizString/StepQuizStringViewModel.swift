import Combine
import Foundation
import shared

final class StepQuizStringViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    private let dataType: StepQuizStringDataType

    @Published var viewData: StepQuizStringViewData
    private var viewDataChangesCancellable: AnyCancellable?

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

        self.viewDataChangesCancellable = self.$viewData.sink { [weak self] newViewData in
            guard let strongSelf = self,
                  strongSelf.viewData.text != newViewData.text else {
                return
            }

            strongSelf.outputCurrentText(newViewData.text)
        }
    }

    private func outputCurrentText(_ text: String) {
        let reply: Reply = {
            switch self.dataType {
            case .string:
                return Reply(text: text, files: [])
            case .number:
                return Reply(number: text)
            case .math:
                return Reply(formula: text)
            }
        }()

        self.delegate?.handleChildQuizSync(reply: reply)
    }
}
