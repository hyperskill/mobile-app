import Combine
import Foundation
import shared

final class StepQuizSortingViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let dataset: Dataset
    private let reply: Reply?

    @Published private(set) var viewData: StepQuizSortingViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        if let options = dataset.options {
            let items: [StepQuizSortingViewData.Option]

            if let ordering = reply?.ordering as? [Int] {
                items = ordering.map { .init(id: $0, text: options[$0]) }
            } else {
                items = options.enumerated().map { .init(id: $0, text: $1) }
            }

            self.viewData = StepQuizSortingViewData(items: items)
        } else {
            self.viewData = StepQuizSortingViewData(items: [])
        }
    }

    func doMoveUp(from index: Int) {
        let tmp = viewData.items[index - 1]
        viewData.items[index - 1] = viewData.items[index]
        viewData.items[index] = tmp

        outputCurrentReply()
    }

    func doMoveDown(from index: Int) {
        let tmp = viewData.items[index + 1]
        viewData.items[index + 1] = viewData.items[index]
        viewData.items[index] = tmp

        outputCurrentReply()
    }

    func createReply() -> Reply {
        Reply.Companion.shared.sorting(
            ordering: viewData.items.map { KotlinInt(value: Int32($0.id)) }
        )
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
