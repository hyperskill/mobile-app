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

        if let pairs = dataset.pairs {
            let items: [StepQuizMatchingViewData.MatchItem]

            if let ordering = reply?.ordering {
                items = ordering.enumerated().map { index, order in
                    .init(
                        title: .init(id: index, text: pairs[index].first ?? ""),
                        option: .init(id: order.intValue, text: pairs[order.intValue].second ?? "")
                    )
                }
                self.options = items.compactMap(\.option)
            } else {
                var options = [StepQuizMatchingViewData.MatchItem.Option]()
                items = pairs.enumerated().map { index, pair in
                    options.append(.init(id: index, text: pair.second ?? ""))
                    return .init(title: .init(id: index, text: pair.first ?? ""))
                }
                self.options = options
            }

            self.viewData = StepQuizMatchingViewData(items: items)
        } else {
            self.options = []
            self.viewData = StepQuizMatchingViewData(items: [])
        }
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
                guard let self,
                      let selectedColumnID = selectedColumnsIDs.first,
                      matchItem.option?.id != selectedColumnID,
                      let currentItemIndex = self.viewData.items.firstIndex(of: matchItem) else {
                    return
                }

                if let swappingIndex = self.viewData.items.firstIndex(where: { $0.option?.id == selectedColumnID }) {
                    let tmp = self.viewData.items[currentItemIndex].option
                    self.viewData.items[currentItemIndex].option = self.viewData.items[swappingIndex].option
                    self.viewData.items[swappingIndex].option = tmp
                } else {
                    self.viewData.items[currentItemIndex].option = self.options.first(
                        where: { $0.id == selectedColumnID }
                    )
                }

                self.outputCurrentReply()
            }
        )
    }

    func createReply() -> Reply {
        Reply(ordering: viewData.items.compactMap(\.option).map(\.id))
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
