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
            var items = [StepQuizMatchingViewData.Item]()

            if let ordering = reply?.ordering {
                for (index, order) in ordering.enumerated() {
                    items.append(.title(text: pairs[index].first ?? ""))
                    items.append(.option(.init(id: order.intValue, text: pairs[order.intValue].second ?? "")))
                }
            } else {
                for (index, pair) in pairs.enumerated() {
                    items.append(.title(text: pair.first ?? ""))
                    items.append(.option(.init(id: index, text: pair.second ?? "")))
                }
            }

            self.viewData = StepQuizMatchingViewData(items: items)
        } else {
            self.viewData = StepQuizMatchingViewData(items: [])
        }
    }

    func doMoveUp(from index: Int) {
        assert(!index.isMultiple(of: 2), "Index must be odd")

        let tmp = viewData.items[index - 2]
        viewData.items[index - 2] = viewData.items[index]
        viewData.items[index] = tmp

        outputCurrentReply()
    }

    func doMoveDown(from index: Int) {
        assert(!index.isMultiple(of: 2), "Index must be odd")

        let tmp = viewData.items[index + 2]
        viewData.items[index + 2] = viewData.items[index]
        viewData.items[index] = tmp

        outputCurrentReply()
    }

    func createReply() -> Reply {
        Reply(
            ordering: viewData.items.compactMap { item in
                switch item {
                case .title:
                    nil
                case .option(let option):
                    option.id
                }
            }
        )
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
