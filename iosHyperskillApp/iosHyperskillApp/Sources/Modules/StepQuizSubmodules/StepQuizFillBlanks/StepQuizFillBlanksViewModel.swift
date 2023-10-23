import Combine
import Foundation
import shared

final class StepQuizFillBlanksViewModel: ObservableObject {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?
    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    @Published private(set) var viewData: StepQuizFillBlanksViewData

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        viewDataMapper: StepQuizFillBlanksViewDataMapper,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.provideModuleInputCallback = provideModuleInputCallback
        self.viewData = viewDataMapper.mapToViewData(dataset: dataset, reply: reply)
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
    }

    func doInputTextUpdate(_ inputText: String, for component: StepQuizFillBlankComponent) {
        guard let index = viewData.components.firstIndex(
            where: { $0.id == component.id }
        ) else {
            return
        }

        viewData.components[index].inputText = inputText
        outputCurrentReply()
    }

    func doSelectComponent(at indexPath: IndexPath) {
        setIsFirstResponder(true, forComponentAt: indexPath)
    }

    func doDeselectComponent(at indexPath: IndexPath) {
        setIsFirstResponder(false, forComponentAt: indexPath)
    }

    private func setIsFirstResponder(_ isFirstResponder: Bool, forComponentAt indexPath: IndexPath) {
        guard viewData.components[indexPath.row].type == .input else {
            return
        }

        viewData.components[indexPath.row].isFirstResponder = isFirstResponder
    }
}

// MARK: - StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol -

extension StepQuizFillBlanksViewModel: StepQuizChildQuizInputProtocol {
    func createReply() -> Reply {
        let blanks: [String] = viewData.components.compactMap { component in
            switch component.type {
            case .text, .lineBreak:
                return nil
            case .input:
                return component.inputText ?? ""
            }
        }

        return Reply.companion.fillBlanks(blanks: blanks)
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
