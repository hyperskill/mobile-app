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

    let stackRouter: StackRouterProtocol
    let panModalPresenter: PanModalPresenter

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
                .animation(.default, value: viewModel.state)
        }
        .navigationTitle(Strings.Home.title)
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

                    let challengeWidgetViewStateKs = viewModel.challengeWidgetViewStateKs
                    if challengeWidgetViewStateKs != .empty {
                        ChallengeWidgetAssembly(
                            challengeWidgetViewStateKs: challengeWidgetViewStateKs,
                            moduleOutput: viewModel
                        )
                        .makeModule()
                        .equatable()
                    }

                    ProblemOfDayAssembly(
                        problemOfDayState: data.problemOfDayState,
                        isFreemiumEnabled: data.isProblemsLimitEnabled,
                        output: viewModel
                    )
                    .makeModule()

                    if let availableRepetitionsState = data.repetitionsState as? HomeFeatureRepetitionsStateAvailable {
                        TopicsRepetitionsCardView(
                            topicsToRepeatCount: Int(availableRepetitionsState.recommendedRepetitionsCount),
                            onTap: viewModel.doTopicsRepetitionsPresentation,
                            isFreemiumEnabled: data.isProblemsLimitEnabled
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
}

// MARK: - HomeView (ViewAction) -

private extension HomeView {
    func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
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
            GamificationToolbarViewActionHandler.handle(
                viewAction: gamificationToolbarViewAction.viewAction,
                stackRouter: stackRouter,
                panModalPresenter: panModalPresenter
            )
        case .challengeWidgetViewAction(let challengeWidgetViewAction):
            handleChallengeWidgetViewAction(
                challengeWidgetViewAction.viewAction
            )
        }
    }

    func handleChallengeWidgetViewAction(_ viewAction: ChallengeWidgetFeatureActionViewAction) {
        switch ChallengeWidgetFeatureActionViewActionKs(viewAction) {
        case .openUrl(let openUrlViewAction):
            WebControllerManager.shared.presentWebControllerWithURLString(
                openUrlViewAction.url,
                withKey: .externalLink,
                controllerType: openUrlViewAction.shouldOpenInApp ? .inAppSafari : .safari
            )
        case .showNetworkError:
            ProgressHUD.showError()
        }
    }
}
