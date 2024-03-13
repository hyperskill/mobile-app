import Foundation
import shared

final class ManageSubscriptionViewModel: FeatureViewModel<
  ManageSubscriptionFeatureViewState,
  ManageSubscriptionFeatureMessage,
  ManageSubscriptionFeatureActionViewAction
> {
    var stateKs: ManageSubscriptionFeatureViewStateKs { .init(state) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(ManageSubscriptionFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: ManageSubscriptionFeatureViewState,
        newState: ManageSubscriptionFeatureViewState
    ) -> Bool {
        ManageSubscriptionFeatureViewStateKs(oldState) != ManageSubscriptionFeatureViewStateKs(newState)
    }

    func doRetryLoadManageSubscription() {
        onNewMessage(ManageSubscriptionFeatureMessageRetryContentLoading())
    }

    func doCallToAction() {
        onNewMessage(ManageSubscriptionFeatureMessageActionButtonClicked())
    }

    func logViewedEvent() {
        onNewMessage(ManageSubscriptionFeatureMessageViewedEventMessage())
    }
}
