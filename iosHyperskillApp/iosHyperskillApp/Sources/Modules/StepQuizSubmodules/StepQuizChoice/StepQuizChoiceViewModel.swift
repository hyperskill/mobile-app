import Combine
import Foundation
import shared

final class StepQuizChoiceViewModel: ObservableObject, StepQuizChildQuizInputProtocol {
    weak var moduleOutput: StepQuizChildQuizOutputProtocol?

    private let dataset: Dataset
    private let reply: Reply?

    @Published private(set) var viewData: StepQuizChoiceViewData

    init(dataset: Dataset, reply: Reply?) {
        self.dataset = dataset
        self.reply = reply

        let choices: [Bool]
        if let sortingAnswers = reply?.choices as? [ChoiceAnswerChoice] {
            choices = sortingAnswers.map(\.boolValue)
        } else {
            choices = Array(repeating: false, count: dataset.options?.count ?? 0)
        }

        self.viewData = StepQuizChoiceViewData(
            isMultipleChoice: dataset.isMultipleChoice,
            choices: zip(dataset.options ?? [], choices).map { .init(text: $0, isSelected: $1) }
        )
    }

    func createReply() -> Reply {
        Reply(sortingChoices: viewData.choices.map(\.isSelected))
    }

    func doChoiceSelection(at selectedIndex: Int) {
        if viewData.isMultipleChoice {
            viewData.choices[selectedIndex].isSelected.toggle()
        } else {
            for index in viewData.choices.indices {
                viewData.choices[index].isSelected = index == selectedIndex ? true : false
            }
        }

        moduleOutput?.handleChildQuizSync(reply: createReply())
    }
}
