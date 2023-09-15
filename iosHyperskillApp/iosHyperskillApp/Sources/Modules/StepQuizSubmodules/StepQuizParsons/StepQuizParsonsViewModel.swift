import Combine
import Foundation
import shared

final class StepQuizParsonsViewModel: ObservableObject {
    private static let tabsMaxCount = 10

    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    @Published private(set) var viewData: StepQuizParsonsViewData

    private var selectedLineNumberIndex: Int? {
        guard let selectedLineNumber = viewData.selectedLineNumber else {
            return nil
        }
        return viewData.lines.firstIndex(where: { $0.lineNumber == selectedLineNumber })
    }

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        viewDataMapper: StepQuizParsonsViewDataMapper,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.provideModuleInputCallback = provideModuleInputCallback
        self.viewData = viewDataMapper.mapToViewData(step: step, dataset: dataset, reply: reply)
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
    }

    func doSelectLine(lineNumber: Int) {
        if viewData.selectedLineNumber == lineNumber {
            viewData.selectedLineNumber = nil
        } else {
            viewData.selectedLineNumber = lineNumber
        }
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

    private func doMove(indexAddition: Int) {
        guard let selectedLineNumberIndex else {
            return
        }

        let tmp = viewData.lines[selectedLineNumberIndex + indexAddition]
        viewData.lines[selectedLineNumberIndex + indexAddition] = viewData.lines[selectedLineNumberIndex]
        viewData.lines[selectedLineNumberIndex] = tmp

        outputCurrentReply()
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}

// MARK: - StepQuizParsonsViewModel: StepQuizChildQuizInputProtocol -

extension StepQuizParsonsViewModel: StepQuizChildQuizInputProtocol {
    func createReply() -> Reply {
        Reply.companion.parsons(
            lines: viewData.lines.map {
                .init(level: Int32($0.level), lineNumber: Int32($0.lineNumber))
            }
        )
    }
}
