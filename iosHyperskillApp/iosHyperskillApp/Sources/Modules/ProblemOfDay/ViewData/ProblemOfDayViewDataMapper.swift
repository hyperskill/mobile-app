import Foundation
import shared

final class ProblemOfDayViewDataMapper {
    private let formatter: Formatter

    init(formatter: Formatter) {
        self.formatter = formatter
    }

    func mapProblemOfDayStateToViewData(
        _ problemOfDayState: HomeFeatureProblemOfDayState
    ) -> ProblemOfDayViewData {
        let needToSolveStateOrNil = problemOfDayState as? HomeFeatureProblemOfDayStateNeedToSolve
        let solvedStateOrNil = problemOfDayState as? HomeFeatureProblemOfDayStateSolved

        let state: ProblemOfDayCardView.State = {
            switch problemOfDayState {
            case is HomeFeatureProblemOfDayStateEmpty:
                return .unavailable
            case is HomeFeatureProblemOfDayStateNeedToSolve:
                return .uncompleted
            case is HomeFeatureProblemOfDayStateSolved:
                return .completed
            default:
                assertionFailure(
                    "ProblemOfDayViewDataMapper :: did receive unsopported problemOfDayState = \(problemOfDayState)"
                )
                return .unavailable
            }
        }()

        let formattedTimeToSolve: String? = {
            let step = solvedStateOrNil?.step ?? needToSolveStateOrNil?.step

            guard let secondsToComplete = step?.secondsToComplete else {
                return nil
            }

            return formatter.minutesOrSecondsCount(seconds: secondsToComplete.doubleValue)
        }()

        let nextProblemIn = solvedStateOrNil?.nextProblemIn ?? needToSolveStateOrNil?.nextProblemIn

        let needToRefresh: Bool = solvedStateOrNil?.needToRefresh ?? needToSolveStateOrNil?.needToRefresh ?? false

        let stepID: Int? = {
            guard let stepID = solvedStateOrNil?.step.id ?? needToSolveStateOrNil?.step.id else {
                return nil
            }

            return Int(stepID)
        }()

        return ProblemOfDayViewData(
            state: state,
            timeToSolve: formattedTimeToSolve,
            nextProblemIn: nextProblemIn,
            needToRefresh: needToRefresh,
            stepID: stepID
        )
    }
}
