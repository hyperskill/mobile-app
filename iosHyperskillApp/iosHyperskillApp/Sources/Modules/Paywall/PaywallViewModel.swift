import Foundation
import shared

final class PaywallViewModel: FeatureViewModel<
  PaywallFeature.ViewState,
  PaywallFeatureMessage,
  PaywallFeatureActionViewAction
> {
    weak var moduleOutput: PaywallOutputProtocol?

    var contentStateKs: PaywallFeatureViewStateContentKs { .init(state.contentState) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(PaywallFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: PaywallFeature.ViewState,
        newState: PaywallFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doRetryContentLoading() {
        onNewMessage(PaywallFeatureMessageRetryContentLoading())
    }

    func doBuySubscription() {
        onNewMessage(PaywallFeatureMessageBuySubscriptionClicked(purchaseParams: IOSPurchaseParams.shared))
    }

    func doContinueWithLimits() {
        onNewMessage(PaywallFeatureMessageContinueWithLimitsClicked())
    }

    func doTermsOfServicePresentation() {
        onNewMessage(PaywallFeatureMessageClickedTermsOfServiceAndPrivacyPolicy())
    }

    func doCompletePaywall() {
        moduleOutput?.handlePaywallCompleted()
    }

    func logViewedEvent() {
        onNewMessage(PaywallFeatureMessageViewedEventMessage())
    }
}
