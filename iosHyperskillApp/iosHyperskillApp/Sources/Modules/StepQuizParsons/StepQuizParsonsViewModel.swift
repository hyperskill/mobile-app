import Combine
import Foundation
import shared

final class StepQuizParsonsViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

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

        if let datasetLines = dataset.lines {
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
        } else {
            self.viewData = StepQuizParsonsViewData(lines: [])
        }
    }

    func doSelectLine(lineNumber: Int) {
        viewData.selectedLineNumber = lineNumber
    }

    func doAddTab() {
        guard let index = selectedLineNumberIndex else {
            return
        }
        viewData.lines[index].level += 1
        outputCurrentReply()
    }

    func isAddTabDisabled() -> Bool {
        guard let index = selectedLineNumberIndex else {
            return false
        }
        return viewData.lines[index].level == 3
    }

    func doRemoveTab() {
        guard let index = selectedLineNumberIndex else {
            return
        }
        viewData.lines[index].level -= 1
        outputCurrentReply()
    }

    func isRemoveTabDisabled() -> Bool {
        guard let index = selectedLineNumberIndex else {
            return false
        }
        return viewData.lines[index].level == 0
    }

    func doMoveUp() {
        guard let index = selectedLineNumberIndex else {
            return
        }
        let tmp = viewData.lines[index - 1]
        viewData.lines[index - 1] = viewData.lines[index]
        viewData.lines[index] = tmp

        outputCurrentReply()
    }

    func isMoveUpDisabled() -> Bool {
        guard let index = selectedLineNumberIndex else {
            return false
        }
        return index == 0
    }

    func doMoveDown() {
        guard let index = selectedLineNumberIndex else {
            return
        }
        let tmp = viewData.lines[index + 1]
        viewData.lines[index + 1] = viewData.lines[index]
        viewData.lines[index] = tmp

        outputCurrentReply()
    }

    func isMoveDownDisabled() -> Bool {
        guard let index = selectedLineNumberIndex else {
            return false
        }
        return index == viewData.lines.count - 1
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
}
