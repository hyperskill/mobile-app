import Foundation

enum StepQuizActionButtonCodeQuizDelegate {
    typealias Params = (title: String, systemImageName: String)

    static func getTitle(for state: StepQuizActionButton.State) -> String? {
        getParamsForState(state)?.title
    }

    static func getSystemImageName(for state: StepQuizActionButton.State) -> String? {
        getParamsForState(state)?.systemImageName
    }

    static func getParamsForState(_ state: StepQuizActionButton.State) -> Params? {
        switch state {
        case .normal, .wrong, .evaluation:
            return (Strings.StepQuizCode.runSolutionButton, "play")
        case .correct, .correctLoading:
            return nil
        }
    }
}
