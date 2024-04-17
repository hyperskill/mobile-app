import Foundation
import shared

final class ProblemsLimitReachedModalViewModel: FeatureViewModel<
  ProblemsLimitReachedModalFeature.ViewState,
  ProblemsLimitReachedModalFeatureMessage,
  ProblemsLimitReachedModalFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: ProblemsLimitReachedModalFeature.ViewState,
        newState: ProblemsLimitReachedModalFeature.ViewState
    ) -> Bool {
        false
    }

    func doGoToHomeScreen() {
        onNewMessage(ProblemsLimitReachedModalFeatureMessageGoToHomeScreenClicked())
    }

    func doUnlockUnlimitedProblems() {
        onNewMessage(ProblemsLimitReachedModalFeatureMessageUnlockUnlimitedProblemsClicked())
    }

    func logShownEvent() {
        onNewMessage(ProblemsLimitReachedModalFeatureMessageShownEventMessage())
    }

    func logHiddenEvent() {
        onNewMessage(ProblemsLimitReachedModalFeatureMessageHiddenEventMessage())
    }
}
