import Combine
import Foundation
import shared

final class StepQuizSortingViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    @Published private(set) var viewData: StepQuizSortingViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        if let options = dataset.options {
            let items: [StepQuizSortingViewData.Option]

            if let ordering = reply?.ordering {
                items = ordering.map { .init(id: $0.intValue, text: options[$0.intValue]) }
            } else {
                items = options.enumerated().map { .init(id: $0, text: $1) }
            }

            self.viewData = StepQuizSortingViewData(items: items)
        } else {
            self.viewData = StepQuizSortingViewData(items: [])
        }
    }

    func doMoveUp(from index: Int) {
        let tmp = self.viewData.items[index - 1]
        self.viewData.items[index - 1] = self.viewData.items[index]
        self.viewData.items[index] = tmp

        self.outputCurrentReply()
    }

    func doMoveDown(from index: Int) {
        let tmp = self.viewData.items[index + 1]
        self.viewData.items[index + 1] = self.viewData.items[index]
        self.viewData.items[index] = tmp

        self.outputCurrentReply()
    }

    private func outputCurrentReply() {
        let reply = Reply(ordering: self.viewData.items.map(\.id))
        self.delegate?.handleChildQuizSync(reply: reply)
    }
}
