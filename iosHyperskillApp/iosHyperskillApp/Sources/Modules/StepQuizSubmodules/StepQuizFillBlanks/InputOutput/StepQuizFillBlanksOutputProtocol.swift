import Foundation

protocol StepQuizFillBlanksOutputProtocol: StepQuizChildQuizOutputProtocol {
    func handleStepQuizFillBlanksCurrentSelectModeState(
        options: [StepQuizFillBlankOption],
        selectedIndices: Set<Int>,
        blanksCount: Int
    )
}
