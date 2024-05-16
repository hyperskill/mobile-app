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

    func makeSelectColumnsViewController(
        for matchItem: StepQuizMatchingViewData.MatchItem
    ) -> PanModalPresentableViewController {
        let columns = viewData.items.map { item in
            StepQuizTableViewData.Column(id: item.option.id, text: item.option.text)
        }

        return StepQuizTableSelectColumnsViewController(
            title: matchItem.title.text,
            columns: columns,
            selectedColumnsIDs: [matchItem.option.id],
            isMultipleChoice: false,
            onColumnsSelected: { [weak self] selectedColumnsIDs in
                guard let self else {
                    return
                }

                guard let selectedColumnID = selectedColumnsIDs.first,
                      matchItem.option.id != selectedColumnID else {
                    return
                }

                guard let targetIndex = self.viewData.items.firstIndex(
                    where: { $0.option.id == selectedColumnID }
                ), let originalIndex = self.viewData.items.firstIndex(of: matchItem) else {
                    return
                }

                let tmp = self.viewData.items[originalIndex].option
                self.viewData.items[originalIndex].option = self.viewData.items[targetIndex].option
                self.viewData.items[targetIndex].option = tmp

                self.outputCurrentReply()
            }
        )
    }

    func createReply() -> Reply {
        Reply(ordering: viewData.items.map(\.option.id))
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
