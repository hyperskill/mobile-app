import Foundation
import shared

final class ProblemOfDayViewDataMapper {
    private let formatter: Formatter

    init (formatter: Formatter) {
        self.formatter = formatter
    }

    func mapProblemOfDayStateToViewData(
        _ problemOfDayState: HomeFeatureProblemOfDayState
    ) -> ProblemOfDayViewData {
        let dataNeedToSolve = problemOfDayState as? HomeFeatureProblemOfDayStateNeedToSolve
        let dataSolved = problemOfDayState as? HomeFeatureProblemOfDayStateSolved

        let state: ProblemOfDayCardView.ProblemOfDayState = {
            if dataSolved != nil {
                return .completed
            } else if dataNeedToSolve != nil {
                return .uncompleted
            } else if problemOfDayState as? HomeFeatureProblemOfDayStateEmpty != nil {
                return .unavailable
            }
            return .unavailable
        }()

        let timeToSolve: String? = {
            if let dataSolved = dataSolved, let secondsToComplete = dataSolved.step.secondsToComplete {
                return formatter.minutesCount(seconds: Int(truncating: secondsToComplete))
            }
            if let dataNeedToSolve = dataNeedToSolve, let secondsToComplete = dataNeedToSolve.step.secondsToComplete {
                return formatter.minutesCount(seconds: Int(truncating: secondsToComplete))
            }
            return nil
        }()

        let nextProblemIn: String? = {
            if let dataSolved = dataSolved {
                return formatter.hoursMinutesCount(seconds: Int(dataSolved.nextProblemIn))
            }
            if let dataNeedToSolve = dataNeedToSolve {
                return formatter.hoursMinutesCount(seconds: Int(dataNeedToSolve.nextProblemIn))
            }
            return nil
        }()

        let needToRefresh: Bool = {
            if let dataSolved = dataSolved {
                return Int(dataSolved.nextProblemIn) <= 0
            }
            if let dataNeedToSolve = dataNeedToSolve {
                return Int(dataNeedToSolve.nextProblemIn) <= 0
            }
            return false
        }()

        return ProblemOfDayViewData(
            state: state,
            timeToSolve: timeToSolve,
            nextProblemIn: nextProblemIn,
            needToRefresh: needToRefresh
        )
    }
}
