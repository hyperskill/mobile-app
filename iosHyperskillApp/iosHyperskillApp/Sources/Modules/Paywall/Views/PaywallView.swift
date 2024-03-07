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
            break
        case .completePaywall:
            break
        case .navigateTo:
            break
        case .openUrl:
            break
        case .showMessage:
            break
        case .studyPlan:
            break
        }
    }
}
