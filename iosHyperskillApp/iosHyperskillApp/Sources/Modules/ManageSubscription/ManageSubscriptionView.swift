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
            Text("Hello, World!")
        }
    }
}

// MARK: - ManageSubscriptionView (ViewAction) -

private extension ManageSubscriptionView {
    func handleViewAction(
        _ viewAction: ManageSubscriptionFeatureActionViewAction
    ) {
        switch ManageSubscriptionFeatureActionViewActionKs(viewAction) {
        case .navigateTo:
            break
        case .openUrl:
            break
        }
    }
}
