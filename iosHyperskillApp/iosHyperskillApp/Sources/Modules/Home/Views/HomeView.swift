import shared
import SwiftUI

extension HomeView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct HomeView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: HomeViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    @StateObject var panModalPresenter: PanModalPresenter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: {
                    viewModel.logViewedEvent()
                    viewModel.doLoadContent()
                }
            )

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationTitle(Strings.Home.title)
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
        switch viewModel.homeStateKs {
        case .idle:
            HomeSkeletonView()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            HomeSkeletonView()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadContent(forceUpdate: true)
                }
            )
        case .content(let data):
            ScrollView {
                VStack(alignment: .leading, spacing: appearance.spacingBetweenContainers) {
                    HomeSubheadlineView()

                    ProblemOfDayAssembly(
                        problemOfDayState: data.problemOfDayState,
                        isFreemiumEnabled: data.isFreemiumEnabled,
                        output: viewModel
                    )
                    .makeModule()

                    if let availableRepetitionsState = data.repetitionsState as? HomeFeatureRepetitionsStateAvailable {
                        TopicsRepetitionsCardView(
                            topicsToRepeatCount: Int(availableRepetitionsState.recommendedRepetitionsCount),
                            onTap: viewModel.doTopicsRepetitionsPresentation,
                            isFreemiumEnabled: data.isFreemiumEnabled
                        )
                    }
                }
                .padding([.horizontal, .bottom])
                .pullToRefresh(
                    isShowing: Binding(
                        get: { viewModel.state.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
        switch HomeFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch HomeFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .stepScreen(let data):
                let assembly = StepAssembly(stepRoute: data.stepRoute)
                stackRouter.pushViewController(assembly.makeModule())
            case .topicsRepetitionsScreen:
                let assembly = TopicsRepetitionsAssembly()
                stackRouter.pushViewController(assembly.makeModule())
            }
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            switch GamificationToolbarFeatureActionViewActionKs(gamificationToolbarViewAction.viewAction) {
            case .showProfileTab:
                TabBarRouter(tab: .profile).route()
            case .showProgressScreen:
                let assembly = ProgressScreenAssembly()
                stackRouter.pushViewController(assembly.makeModule())
            }
        case .challengeWidgetViewAction:
            #warning("TODO: Handle challenge widget view action")
        }
    }
}

#Preview {
    UIKitViewControllerPreview {
        HomeAssembly().makeModule()
    }
}
