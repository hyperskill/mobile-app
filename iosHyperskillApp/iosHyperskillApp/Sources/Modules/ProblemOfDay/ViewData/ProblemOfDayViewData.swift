import Foundation

struct ProblemOfDayViewData {
    let state: ProblemOfDayCardView.State
    let timeToSolve: String?
    let nextProblemIn: String?
    let needToRefresh: Bool
    let stepID: Int?
}
