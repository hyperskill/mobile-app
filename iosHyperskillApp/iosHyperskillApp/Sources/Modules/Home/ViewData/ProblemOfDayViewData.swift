import Foundation

struct ProblemOfDayViewData {
    let state: ProblemOfDayCardView.ProblemOfDayState
    let timeToSolve: String?
    let nextProblemIn: String?
    let needToRefresh: Bool
}
