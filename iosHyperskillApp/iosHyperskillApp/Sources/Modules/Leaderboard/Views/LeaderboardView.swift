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

    let stackRouter: StackRouterProtocol
    let panModalPresenter: PanModalPresenter

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
                viewStateKs: viewModel.gamificationToolbarViewStateKs,
                onStreakTap: viewModel.doStreakBarButtonItemAction,
                onProgressTap: viewModel.doProgressBarButtonItemAction,
                onProblemsLimitTap: viewModel.doProblemsLimitBarButtonItemAction,
                onSearchTap: viewModel.doSearchBarButtonItemAction
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
        VStack {
            Picker(
                "",
                selection: Binding<LeaderboardTab>(
                    get: { viewModel.state.currentTab.wrapped.require() },
                    set: { viewModel.doSelectTab($0.shared) }
                )
            ) {
                ForEach(LeaderboardTab.allCases, id: \.self) { option in
                    Text(option.title)
                }
            }
            .pickerStyle(.segmented)
            .padding(.horizontal)

            switch viewModel.listViewStateKs {
            case .idle:
                LeaderboardSkeletonView()
                    .onAppear(perform: viewModel.doLoadLeaderboard)
            case .loading:
                LeaderboardSkeletonView()
            case .error:
                PlaceholderView(
                    configuration: .networkError(
                        titleText: Strings.Leaderboard.placeholderErrorDescription,
                        backgroundColor: appearance.backgroundColor,
                        action: viewModel.doRetryLoadLeaderboard
                    )
                )
            case .empty:
                LeaderboardPlaceholderEmptyView()
            case .content(let listViewState):
                LeaderboardListView(
                    items: listViewState.list,
                    updatesInText: viewModel.state.updatesInText,
                    onRowTap: viewModel.doListItemTapAction(item:)
                )
            }
        }
    }
}

// MARK: - LeaderboardView (ViewAction) -

private extension LeaderboardView {
    func handleViewAction(
        _ viewAction: LeaderboardScreenFeatureActionViewAction
    ) {
        switch LeaderboardScreenFeatureActionViewActionKs(viewAction) {
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            GamificationToolbarViewActionHandler.handle(
                viewAction: gamificationToolbarViewAction.viewAction,
                stackRouter: stackRouter,
                panModalPresenter: panModalPresenter
            )
        case .leaderboardWidgetViewAction:
            break
        }
    }
}
