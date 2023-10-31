import Combine
import Foundation
import shared

final class StepQuizFillBlanksViewModel: ObservableObject {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?
    private let provideModuleInputCallback: (StepQuizChildQuizInputProtocol?) -> Void

    private let mode: FillBlanksModeWrapper

    private let viewDataMapper: StepQuizFillBlanksViewDataMapper
    @Published private(set) var viewData: StepQuizFillBlanksViewData

    private var currentSelectBlankComponentIndex: Int?

    init(
        step: Step,
        dataset: Dataset,
        reply: Reply?,
        mode: FillBlanksModeWrapper,
        viewDataMapper: StepQuizFillBlanksViewDataMapper,
        provideModuleInputCallback: @escaping (StepQuizChildQuizInputProtocol?) -> Void
    ) {
        self.mode = mode
        self.provideModuleInputCallback = provideModuleInputCallback

        self.viewDataMapper = viewDataMapper
        self.viewData = viewDataMapper.mapToViewData(dataset: dataset, reply: reply)
        self.setInitialSelectBlankComponentIndex(viewData: &self.viewData)
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
        outputCurrentSelectModeState()
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

    @MainActor
    func doSelectComponent(at indexPath: IndexPath) {
        switch viewData.components[indexPath.row].type {
        case .input:
            inputModeSetIsFirstResponder(true, forComponentAt: indexPath)
        case .select:
            selectModeHandleDidSelectComponent(at: indexPath)
        default:
            break
        }
    }

    func doDeselectComponent(at indexPath: IndexPath) {
        inputModeSetIsFirstResponder(false, forComponentAt: indexPath)
    }

    private func inputModeSetIsFirstResponder(_ isFirstResponder: Bool, forComponentAt indexPath: IndexPath) {
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
            case .select:
                guard let selectedOptionIndex = component.selectedOptionIndex else {
                    return nil
                }

                return viewData.options[selectedOptionIndex].originalText
            }
        }

        return Reply.companion.fillBlanks(blanks: blanks)
    }

    func update(step: Step, dataset: Dataset, reply: Reply?) {
        var viewData = viewDataMapper.mapToViewData(dataset: dataset, reply: reply)
        setInitialSelectBlankComponentIndex(viewData: &viewData)
        self.viewData = viewData

        outputCurrentSelectModeState()
    }

    private func outputCurrentReply() {
        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}

// MARK: - StepQuizFillBlanksViewModel (Select Mode) -

extension StepQuizFillBlanksViewModel {
    private func outputCurrentSelectModeState() {
        guard mode == .select else {
            return
        }

        guard let fillBlanksModuleOutput = moduleOutput as? StepQuizFillBlanksOutputProtocol else {
            assertionFailure("""
StepQuizFillBlanksViewModel: expected StepQuizFillBlanksOutputProtocol, \(String(describing: moduleOutput))
""")
            return
        }

        let options = viewData.options
        let selectedIndices = Set(viewData.components.compactMap(\.selectedOptionIndex))
        let blanksCount = viewData.components.filter({ $0.type == .select }).count

        fillBlanksModuleOutput.handleStepQuizFillBlanksCurrentSelectModeState(
            options: options,
            selectedIndices: selectedIndices,
            blanksCount: blanksCount
        )
    }

    @MainActor
    private func selectModeHandleDidSelectComponent(at indexPath: IndexPath) {
        var components = viewData.components

        let feedbackType: FeedbackGenerator.FeedbackType

        if viewData.components[indexPath.row].selectedOptionIndex != nil {
            components[indexPath.row].selectedOptionIndex = nil
            feedbackType = .impact(.light)
        } else {
            feedbackType = .selection
        }

        currentSelectBlankComponentIndex = indexPath.row

        viewData.components = components.enumerated().map { index, component in
            var component = component
            component.isFirstResponder = index == indexPath.row
            return component
        }

        outputCurrentReply()
        outputCurrentSelectModeState()

        FeedbackGenerator(feedbackType: feedbackType).triggerFeedback()
    }

    private func setInitialSelectBlankComponentIndex(viewData: inout StepQuizFillBlanksViewData) {
        guard mode == .select,
              let index = Self.getFirstSelectBlankComponentIndex(components: viewData.components) else {
            return
        }

        currentSelectBlankComponentIndex = index
        viewData.components[index].isFirstResponder = true
    }

    private static func getFirstSelectBlankComponentIndex(components: [StepQuizFillBlankComponent]) -> Int? {
        components.firstIndex(where: { $0.type == .select && $0.selectedOptionIndex == nil })
    }
}

extension StepQuizFillBlanksViewModel: StepQuizFillBlanksSelectOptionsOutputProtocol {
    func handleStepQuizFillBlanksSelectOptionsDidSelectOption(option: StepQuizFillBlankOption, at index: Int) {
        guard mode == .select else {
            return assertionFailure("StepQuizFillBlanksViewModel: unexpected state")
        }

        guard let currentSelectBlankComponentIndex else {
            return assertionFailure("StepQuizFillBlanksViewModel: currentSelectBlankComponentIndex is nil")
        }

        var components = viewData.components

        components[currentSelectBlankComponentIndex].selectedOptionIndex = index
        components[currentSelectBlankComponentIndex].isFirstResponder = false

        if let index = Self.getFirstSelectBlankComponentIndex(components: components) {
            components[index].isFirstResponder = true
            self.currentSelectBlankComponentIndex = index
        } else {
            self.currentSelectBlankComponentIndex = nil
        }

        viewData.components = components
        outputCurrentReply()
    }
}
