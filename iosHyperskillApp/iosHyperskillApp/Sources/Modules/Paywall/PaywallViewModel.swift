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

    func doScreenShowedAction() {
        onNewMessage(PaywallFeatureMessageViewedEventMessage())
        onNewMessage(PaywallFeatureMessageScreenShowed())
    }

    func doScreenHiddenAction() {
        onNewMessage(PaywallFeatureMessageScreenHidden())
    }

    func doRetryContentLoading() {
        onNewMessage(PaywallFeatureMessageRetryContentLoading())
    }

    func doBuySubscription() {
        onNewMessage(PaywallFeatureMessageBuySubscriptionClicked(purchaseParams: PlatformPurchaseParams()))
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
}

// MARK: - PaywallViewModel (NotificationCenter) -

extension PaywallViewModel {
    func doNotifyPaywallIsShown(isPaywallShown: Bool) {
        NotificationCenter.default.post(
            name: .paywallIsShownDidChange,
            object: nil,
            userInfo: [
                PaywallIsShownNotification.PayloadKey.isPaywallShown.rawValue: isPaywallShown
            ]
        )
    }
}

enum PaywallIsShownNotification {
    fileprivate static let notificationName = Foundation.Notification.Name("paywallIsShownDidChange")

    enum PayloadKey: String {
        case isPaywallShown
    }
}

extension Foundation.Notification.Name {
    static let paywallIsShownDidChange = PaywallIsShownNotification.notificationName
}
