import Combine
import Foundation
import shared

final class StepQuizMatchingViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

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
                        option: .init(id: order.intValue, text: pairs[order.intValue].second ?? "")
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
        let tmp = viewData.items[index - 1].option
        viewData.items[index - 1].option = viewData.items[index].option
        viewData.items[index].option = tmp

        outputCurrentReply()
    }

    func doMoveDown(from index: Int) {
        let tmp = viewData.items[index + 1].option
        viewData.items[index + 1].option = viewData.items[index].option
        viewData.items[index].option = tmp

        outputCurrentReply()
    }

    func createReply() -> Reply {
        Reply(ordering: viewData.items.map(\.option.id))
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
