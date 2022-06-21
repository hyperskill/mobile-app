import Combine
import Foundation
import shared

final class StepQuizTableViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

    private let dataset: Dataset
    private let reply: Reply?

    @Published var viewData: StepQuizTableViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        if let rows = dataset.rows, let columns = dataset.columns {
            let resultRows: [StepQuizTableViewData.Row]

            if let tableAnswers = reply?.choices as? [ChoiceAnswerTable] {
                resultRows = tableAnswers.map { tableAnswer in
                    .init(
                        text: tableAnswer.tableChoice.nameRow,
                        answers: tableAnswer.tableChoice.columns.filter(\.answer).map { .init(text: $0.name) }
                    )
                }
            } else {
                resultRows = rows.map { .init(text: $0, answers: []) }
            }

            self.viewData = StepQuizTableViewData(
                rows: resultRows,
                columns: columns.map { .init(text: $0) },
                isMultipleChoice: dataset.isCheckbox
            )
        } else {
            self.viewData = StepQuizTableViewData(rows: [], columns: [], isMultipleChoice: false)
        }
    }

    func makeSelectColumnsViewController(for row: StepQuizTableViewData.Row) -> PanModalPresentableViewController {
        let viewController = StepQuizTableSelectColumnsViewController(
            row: row,
            columns: self.viewData.columns,
            selectedColumnsIDs: Set(row.answers.map(\.id)),
            isMultipleChoice: self.viewData.isMultipleChoice,
            onColumnsSelected: { [weak self] selectedColumnsIDs in
                guard let strongSelf = self else {
                    return
                }

                guard let targetRowIndex = strongSelf.viewData.rows.firstIndex(where: { $0.id == row.id }) else {
                    return
                }

                strongSelf.viewData.rows[targetRowIndex].answers
                    = strongSelf.viewData.columns.filter { selectedColumnsIDs.contains($0.id) }

                strongSelf.outputCurrentReply()
            }
        )

        return viewController
    }

    private func outputCurrentReply() {
        let reply = Reply(
            tableChoices: self.viewData.rows.map { row in
                TableChoiceAnswer(
                    nameRow: row.text,
                    columns: self.viewData.columns.map { column in
                        .init(id: column.text, answer: row.answers.contains(where: { $0.id == column.id }))
                    }
                )
            }
        )
        self.delegate?.handleChildQuizSync(reply: reply)
    }
}
