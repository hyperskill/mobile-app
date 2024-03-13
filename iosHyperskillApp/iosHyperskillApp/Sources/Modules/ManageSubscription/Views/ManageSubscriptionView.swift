import shared
import SwiftUI

extension ManageSubscriptionView {
    struct Appearance {
        let backgroundColor = Color.background
    }
}

struct ManageSubscriptionView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ManageSubscriptionViewModel

    let stackRouter: StackRouterProtocol

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
        .navigationTitle(Strings.ManageSubscription.navigationTitle)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadManageSubscription
                )
            )
        case .content(let viewData):
            ManageSubscriptionContentView(
                validUntilText: viewData.validUntilFormatted,
                callToActionButtonText: viewData.buttonText,
                onCallToActionButtonTap: viewModel.doCallToAction
            )
        }
    }
}

// MARK: - ManageSubscriptionView (ViewAction) -

private extension ManageSubscriptionView {
    func handleViewAction(
        _ viewAction: ManageSubscriptionFeatureActionViewAction
    ) {
        switch ManageSubscriptionFeatureActionViewActionKs(viewAction) {
        case .openUrl(let data):
            guard let url = URL(string: data.url) else {
                return WebControllerManager.shared.presentWebControllerWithURLString(data.url)
            }

            UIApplication.shared.open(url, options: [:]) { success in
                if !success {
                    WebControllerManager.shared.presentWebControllerWithURLString(data.url)
                }
            }
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                ManageSubscriptionFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        }
    }

    func handleNavigateToViewAction(_ viewAction: ManageSubscriptionFeatureActionViewActionNavigateToKs) {
        switch viewAction {
        case .paywall(let data):
            let assembly = PaywallAssembly(
                context: .init(
                    source: data.paywallTransitionSource,
                    moduleOutput: nil
                )
            )
            stackRouter.pushViewController(assembly.makeModule())
        }
    }
}
