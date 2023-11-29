import shared
import SwiftUI

extension LeaderboardView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
    }
}

struct LeaderboardView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: LeaderboardViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: {
                    viewModel.logViewedEvent()
                    viewModel.doScreenBecomesActive()
                }
            )

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .toolbar {
            GamificationToolbarContent(
                stateKs: viewModel.gamificationToolbarStateKs,
                onGemsTap: viewModel.doGemsBarButtonItemAction,
                onStreakTap: viewModel.doStreakBarButtonItemAction,
                onProgressTap: viewModel.doProgressBarButtonItemAction
            )
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
        case .idle:
            ProgressView()
                .onAppear(perform: viewModel.doLoadLeaderboard)
        case .loading:
            ProgressView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadLeaderboard
                )
            )
        case .content(let viewData):
            Text("Hello, World!")
        }
    }
}

// MARK: - LeaderboardView (ViewAction) -

private extension LeaderboardView {
    func handleViewAction(
        _ viewAction: LeaderboardScreenFeatureActionViewAction
    ) {
        #warning("TODO")
        switch LeaderboardScreenFeatureActionViewActionKs(viewAction) {
        case .gamificationToolbarViewAction:
            break
        case .leaderboardWidgetViewAction:
            break
        }
    }
}

// MARK: - LeaderboardView (Preview) -

@available(iOS 17, *)
#Preview {
    LeaderboardAssembly().makeModule()
}
