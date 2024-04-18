import Foundation
import shared

final class ProblemsLimitInfoModalViewModel: FeatureViewModel<
  ProblemsLimitInfoModalFeature.ViewState,
  ProblemsLimitInfoModalFeatureMessage,
  ProblemsLimitInfoModalFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: ProblemsLimitInfoModalFeature.ViewState,
        newState: ProblemsLimitInfoModalFeature.ViewState
    ) -> Bool {
        false
    }

    func doGoToHomeScreen() {
        onNewMessage(ProblemsLimitInfoModalFeatureMessageGoToHomeScreenClicked())
    }

    func doUnlockUnlimitedProblems() {
        onNewMessage(ProblemsLimitInfoModalFeatureMessageUnlockUnlimitedProblemsClicked())
    }

    func logShownEvent() {
        onNewMessage(ProblemsLimitInfoModalFeatureMessageShownEventMessage())
    }

    func logHiddenEvent() {
        onNewMessage(ProblemsLimitInfoModalFeatureMessageHiddenEventMessage())
    }
}
