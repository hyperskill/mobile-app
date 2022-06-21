import Combine
import Foundation
import shared

final class StepQuizMatchingViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    @Published private(set) var viewData: StepQuizMatchingViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        if let pairs = dataset.pairs {
            let items: [StepQuizMatchingViewData.MatchItem]

            if let ordering = reply?.ordering {
                items = ordering.enumerated().map { index, order in
                    .init(
                        title: .init(id: index, text: pairs[index].first ?? ""),
                        option: .init(id: order.intValue, text: pairs[index].second ?? "")
                    )
                }
            } else {
                items = pairs.enumerated().map { index, pair in
                    .init(
                        title: .init(id: index, text: pair.first ?? ""),
                        option: .init(id: index, text: pair.second ?? "")
                    )
                }
            }

            self.viewData = StepQuizMatchingViewData(items: items)
        } else {
            self.viewData = StepQuizMatchingViewData(items: [])
        }
    }

    func doMoveUp(from index: Int) {
        let tmp = self.viewData.items[index - 1].option
        self.viewData.items[index - 1].option = self.viewData.items[index].option
        self.viewData.items[index].option = tmp

        self.outputCurrentReply()
    }

    func doMoveDown(from index: Int) {
        let tmp = self.viewData.items[index + 1].option
        self.viewData.items[index + 1].option = self.viewData.items[index].option
        self.viewData.items[index].option = tmp

        self.outputCurrentReply()
    }

    private func outputCurrentReply() {
        let reply = Reply(ordering: self.viewData.items.map(\.option.id))
        self.delegate?.handleChildQuizSync(reply: reply)
    }
}
