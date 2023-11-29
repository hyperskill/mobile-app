import shared
import SwiftUI

extension LeaderboardScreenView {
    struct Appearance {
        let backgroundColor = Color.background
    }
}

struct LeaderboardScreenView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: LeaderboardScreenViewModel

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
        switch viewModel.listViewStateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadLeaderboardScreen
                )
            )
        case .content(let viewData):
            Text("Hello, World!")
        }
    }
}

// MARK: - LeaderboardScreenView (ViewAction) -

private extension LeaderboardScreenView {
    func handleViewAction(
        _ viewAction: LeaderboardScreenFeatureActionViewAction
    ) {
        switch LeaderboardScreenFeatureActionViewActionKs(viewAction) {
        case .gamificationToolbarViewAction:
            break
        case .leaderboardWidgetViewAction:
            break
        }
    }
}

// MARK: - LeaderboardScreenView (Preview) -

@available(iOS 17, *)
#Preview {
    LeaderboardScreenAssembly().makeModule()
}
