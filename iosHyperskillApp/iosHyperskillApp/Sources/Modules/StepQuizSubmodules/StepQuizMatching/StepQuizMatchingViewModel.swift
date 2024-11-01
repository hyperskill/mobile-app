import Combine
import Foundation
import shared

final class StepQuizMatchingViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let dataset: Dataset
    private let reply: Reply?

    private let options: [StepQuizMatchingViewData.MatchItem.Option]

    @Published private(set) var viewData: StepQuizMatchingViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        guard let pairs = dataset.pairs else {
            self.options = []
            self.viewData = StepQuizMatchingViewData(items: [])
            return
        }

        let options = pairs.enumerated().map { index, pair in
            StepQuizMatchingViewData.MatchItem.Option(id: index, text: pair.second ?? "")
        }

        let replyOrdering = reply?.ordering as? [Int?]

        let items = pairs.enumerated().map { index, _ in
            StepQuizMatchingViewData.MatchItem(
                title: .init(id: index, text: pairs[safe: index]?.first ?? ""),
                option: options.first { $0.id == replyOrdering?[safe: index] }
            )
        }

        self.options = options
        self.viewData = StepQuizMatchingViewData(items: items)
    }

    func makeSelectColumnsViewController(
        for matchItem: StepQuizMatchingViewData.MatchItem
    ) -> PanModalPresentableViewController {
        let columns = options.map { option in
            StepQuizTableViewData.Column(id: option.id, text: option.text)
        }

        return StepQuizTableSelectColumnsViewController(
            title: matchItem.title.text,
            columns: columns,
            selectedColumnsIDs: matchItem.option != nil ? [matchItem.option.require().id] : [],
            isMultipleChoice: false,
            onColumnsSelected: { [weak self] selectedColumnsIDs in
                guard let strongSelf = self,
                      let selectedColumnID = selectedColumnsIDs.first,
                      matchItem.option?.id != selectedColumnID,
                      let currentItemIndex = strongSelf.viewData.items.firstIndex(of: matchItem) else {
                    return
                }

                if let swapIndex = strongSelf.viewData.items.firstIndex(where: { $0.option?.id == selectedColumnID }) {
                    let tmp = strongSelf.viewData.items[currentItemIndex].option
                    strongSelf.viewData.items[currentItemIndex].option = strongSelf.viewData.items[swapIndex].option
                    strongSelf.viewData.items[swapIndex].option = tmp
                } else {
                    strongSelf.viewData.items[currentItemIndex].option = strongSelf.options.first(
                        where: { $0.id == selectedColumnID }
                    )
                }

                strongSelf.outputCurrentReply()
            }
        )
    }

    func createReply() -> Reply {
        Reply.companion.matching(ordering: viewData.items.map(\.option?.id) as [Any])
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
