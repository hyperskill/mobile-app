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
        let formattedNextProblemIn = nextProblemIn != nil
            ? formatter.hoursWithMinutesCount(seconds: TimeInterval(nextProblemIn.require()))
            : nil

        let needToRefresh: Bool = {
            guard let nextProblemIn = nextProblemIn else {
                return false
            }

            return nextProblemIn <= 0
        }()

        let stepID: Int? = {
            let stepID = solvedStateOrNil?.step.id ?? needToSolveStateOrNil?.step.id

            if let stepID = stepID {
                return Int(stepID)
            } else {
                return nil
            }
        }()

        return ProblemOfDayViewData(
            state: state,
            timeToSolve: formattedTimeToSolve,
            nextProblemIn: formattedNextProblemIn,
            needToRefresh: needToRefresh,
            stepID: stepID
        )
    }
}
