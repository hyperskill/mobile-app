import Foundation
import shared

final class ProblemsLimitViewModel: FeatureViewModel<
  ProblemsLimitFeatureViewState,
  ProblemsLimitFeatureMessage,
  ProblemsLimitFeatureActionViewAction
> {
    var stateKs: ProblemsLimitFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: ProblemsLimitFeatureViewState,
        newState: ProblemsLimitFeatureViewState
    ) -> Bool {
        ProblemsLimitFeatureViewStateKs(oldState) != ProblemsLimitFeatureViewStateKs(newState)
    }

    func loadLimits(forceUpdate: Bool = false) {
        onNewMessage(ProblemsLimitFeatureMessageInitialize(forceUpdate: forceUpdate))
    }
}
