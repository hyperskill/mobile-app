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
            UIViewControllerEventsWrapper(
                onViewWillAppear: viewModel.doScreenShowedAction,
                onViewWillDisappear: viewModel.doScreenHiddenAction
            )

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
                .animation(.default, value: viewModel.state)
            closeButton
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
        .navigationTitle(Strings.Paywall.navigationTitle)
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
                buyFootnoteText: content.trialText,
                onBuyButtonTap: viewModel.doBuySubscription,
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

    @ViewBuilder private var closeButton: some View {
        if !viewModel.state.isToolbarVisible {
            ZStack {
                Button(
                    action: viewModel.doClosePresentation,
                    label: {
                        Image(systemName: "xmark")
                            .imageScale(.large)
                            .padding(.horizontal)
                    }
                )
                .foregroundColor(.newSecondaryText)
            }
            .frame(
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .topTrailing
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
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                PaywallFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
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
        case .notifyPaywallIsShown(let data):
            viewModel.doNotifyPaywallIsShown(isPaywallShown: data.isPaywallShown)
        }
    }

    func handleNavigateToViewAction(_ viewAction: PaywallFeatureActionViewActionNavigateToKs) {
        switch viewAction {
        case .back, .backToProfileSettings:
            presentationMode.wrappedValue.dismiss()
        }
    }
}
