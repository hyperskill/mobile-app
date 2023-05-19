import Foundation
import shared

final class ProblemOfDayViewDataMapper {
    private let dateFormatter: SharedDateFormatter

    init(dateFormatter: SharedDateFormatter) {
        self.dateFormatter = dateFormatter
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

            guard let secondsToComplete = step?.secondsToComplete?.floatValue else {
                return nil
            }

            return dateFormatter.minutesOrSecondsCount(seconds: secondsToComplete)
        }()

        let nextProblemIn = solvedStateOrNil?.nextProblemIn ?? needToSolveStateOrNil?.nextProblemIn

        let needToRefresh = solvedStateOrNil?.needToRefresh ?? needToSolveStateOrNil?.needToRefresh ?? false

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
