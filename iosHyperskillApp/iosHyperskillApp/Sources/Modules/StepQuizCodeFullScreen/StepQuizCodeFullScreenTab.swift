import Foundation

enum StepQuizCodeFullScreenTab: Int, CaseIterable {
    case details
    case code

    var title: String {
        switch self {
        case .details:
            return Strings.StepQuizCode.fullScreenDetailsTab
        case .code:
            return Strings.StepQuizCode.fullScreenCodeTab
        }
    }
}
