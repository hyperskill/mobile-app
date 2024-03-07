import shared
import SwiftUI

extension PaywallView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.newLayer1)
    }
}

struct PaywallView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: PaywallViewModel

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
                .animation(.default, value: viewModel.state)
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.contentStateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    titleText: Strings.Paywall.placeholderErrorDescription,
                    backgroundColor: .clear,
                    action: viewModel.doRetryContentLoading
                )
            )
        case .content(let content):
            PaywallContentView(
                buyButtonText: content.buyButtonText,
                isContinueWithLimitsButtonVisible: content.isContinueWithLimitsButtonVisible,
                onBuyButtonTap: viewModel.doBuySubscription,
                onContinueWithLimitsButtonTap: viewModel.doContinueWithLimits,
                onTermsOfServiceButtonTap: viewModel.doTermsOfServicePresentation
            )
        case .subscriptionSyncLoading:
            PlaceholderView(
                configuration: .imageAndTitle(
                    image: .reload,
                    titleText: Strings.Paywall.subscriptionSyncDescription,
                    backgroundColor: .clear
                )
            )
        }
    }
}

// MARK: - PaywallView (ViewAction) -

private extension PaywallView {
    func handleViewAction(_ viewAction: PaywallFeatureActionViewAction) {
        switch PaywallFeatureActionViewActionKs(viewAction) {
        case .closePaywall:
            presentationMode.wrappedValue.dismiss()
        case .completePaywall:
            viewModel.doCompletePaywall()
        case .navigateTo:
            break
        case .openUrl(let data):
            WebControllerManager.shared.presentWebControllerWithURLString(
                data.url,
                controllerType: .inAppSafari
            )
        case .showMessage(let data):
            let messageKind = data.messageKind
            let message = messageKind.stringRes.localized()

            switch messageKind {
            case .general, .subscriptionWillBecomeAvailableSoon:
                ProgressHUD.showError(status: message)
            default:
                ProgressHUD.show(status: message)
            }
        }
    }
}
