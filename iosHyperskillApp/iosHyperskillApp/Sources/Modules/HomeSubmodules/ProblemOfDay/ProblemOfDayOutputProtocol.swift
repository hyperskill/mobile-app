import Foundation

protocol ProblemOfDayOutputProtocol: AnyObject {
    func handleProblemOfDayReloadRequested()
    func handleProblemOfDayOpenStepRequested(stepID: Int)
}
