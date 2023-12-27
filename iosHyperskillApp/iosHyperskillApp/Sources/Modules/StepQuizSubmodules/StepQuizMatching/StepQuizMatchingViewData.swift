import Foundation

struct StepQuizMatchingViewData {
    var items: [Item]

    enum Item: Identifiable {
        case title(text: String)
        case option(Option)

        var id: String {
            switch self {
            case .title(let text):
                text
            case .option(let option):
                option.text
            }
        }

        struct Option {
            let id: Int
            let text: String
        }
    }
}
