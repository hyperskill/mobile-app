import Combine
import Foundation
import shared

final class StepQuizChoiceViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

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

    func doChoiceSelection(at selectedIndex: Int) {
        if self.viewData.isMultipleChoice {
            self.viewData.choices[selectedIndex].isSelected.toggle()
        } else {
            for index in self.viewData.choices.indices {
                self.viewData.choices[index].isSelected = index == selectedIndex ? true : false
            }
        }

        let reply = Reply(sortingChoices: self.viewData.choices.map(\.isSelected))
        self.delegate?.handleChildQuizSync(reply: reply)
    }
}
