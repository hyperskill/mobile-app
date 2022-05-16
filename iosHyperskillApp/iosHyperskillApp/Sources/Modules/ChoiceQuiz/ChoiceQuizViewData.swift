struct ChoiceQuizViewData {
    let title: String = "Problem's title"
    let desc: String = "Select a statement that will throw an exception."

    var choices: [Choice] = [
        Choice(text: "char[] characters = new char[0];", isSelected: false),
        Choice(text: "char[] characters = new char[1];", isSelected: false),
        Choice(text: "char[] characters = new char[555];", isSelected: false),
        Choice(text: "char[] characters = new char[-5];", isSelected: false)
    ]

    let type: QuizType

    let statsUsers: Int = 2438
    let statsHours: Int = 13
    let status: QuizStatus?


    struct Choice {
        var text: String
        var isSelected: Bool
    }

    var task: String {
        switch type {
        case .single:
            return "Select one option from the list"
        case .multiple:
            return "Select one or more options from the list"
        }
    }
}

enum QuizType {
    case single
    case multiple
}
