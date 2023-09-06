import Combine
import Foundation
import shared

final class StepQuizParsonsViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private static let tabsMaxCount = 10

    private let dataset: Dataset
    private let reply: Reply?

    @Published private(set) var viewData: StepQuizParsonsViewData

    private var selectedLineNumberIndex: Int? {
        guard let selectedLineNumber = viewData.selectedLineNumber else {
            return nil
        }
        return viewData.lines.firstIndex(where: { $0.lineNumber == selectedLineNumber })
    }

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        guard let datasetLines = dataset.lines else {
            self.viewData = StepQuizParsonsViewData(lines: [])
            return
        }

        if let replyLines = reply?.lines {
            self.viewData = StepQuizParsonsViewData(
                lines: replyLines.map {
                    .init(
                        lineNumber: Int($0.lineNumber),
                        text: datasetLines[Int($0.lineNumber)],
                        level: Int($0.level)
                    )
                }
            )
        } else {
            self.viewData = StepQuizParsonsViewData(
                lines: datasetLines
                    .enumerated()
                    .map {
                        .init(lineNumber: $0, text: $1, level: 0)
                    }
            )
        }
    }

    func doSelectLine(lineNumber: Int) {
        viewData.selectedLineNumber = lineNumber
    }

    func doAddTab() {
        guard let selectedLineNumberIndex else {
            return
        }
        viewData.lines[selectedLineNumberIndex].level += 1
        outputCurrentReply()
    }

    func isAddTabDisabled() -> Bool {
        guard let index = selectedLineNumberIndex else {
            return true
        }
        return viewData.lines[index].level == Self.tabsMaxCount
    }

    func doRemoveTab() {
        guard let selectedLineNumberIndex else {
            return
        }
        viewData.lines[selectedLineNumberIndex].level -= 1
        outputCurrentReply()
    }

    func isRemoveTabDisabled() -> Bool {
        guard let selectedLineNumberIndex else {
            return true
        }
        return viewData.lines[selectedLineNumberIndex].level == 0
    }

    func doMoveUp() {
        doMove(indexAddition: -1)
    }

    func isMoveUpDisabled() -> Bool {
        guard let selectedLineNumberIndex else {
            return true
        }
        return selectedLineNumberIndex == 0
    }

    func doMoveDown() {
        doMove(indexAddition: 1)
    }

    func isMoveDownDisabled() -> Bool {
        guard let selectedLineNumberIndex else {
            return true
        }
        return selectedLineNumberIndex == viewData.lines.count - 1
    }

    func createReply() -> Reply {
        Reply.companion.parsons(
            lines: viewData.lines.map {
                .init(level: Int32($0.level), lineNumber: Int32($0.lineNumber))
            }
        )
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }

    private func doMove(indexAddition: Int) {
        guard let selectedLineNumberIndex else {
            return
        }
        let tmp = viewData.lines[selectedLineNumberIndex + indexAddition]
        viewData.lines[selectedLineNumberIndex + indexAddition] = viewData.lines[selectedLineNumberIndex]
        viewData.lines[selectedLineNumberIndex] = tmp

        outputCurrentReply()
    }
}
