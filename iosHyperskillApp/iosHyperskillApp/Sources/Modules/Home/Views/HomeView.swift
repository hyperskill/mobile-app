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
            if data.isLoadingMagicLink {
                let _ = ProgressHUD.show()
            }

            ScrollView {
                VStack(alignment: .leading, spacing: appearance.spacingBetweenContainers) {
                    HomeSubheadlineView()

                    ProblemsLimitView(
                        stateKs: viewModel.problemsLimitViewStateKs,
                        onReloadButtonTap: viewModel.doReloadProblemsLimit
                    )
                    .padding(.top, LayoutInsets.smallInset)

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

                    NextLearningActivityView(
                        appearance: .init(spacing: appearance.spacingBetweenContainers),
                        stateKs: viewModel.nextLearningActivityViewStateKs,
                        onActivityTap: viewModel.doNextLearningActivityPresentation,
                        onReloadButtonTap: viewModel.doReloadNextLearningActivity
                    )
                    .padding(.top)

                    let shouldShowContinueInWebButton = data.problemOfDayState is HomeFeatureProblemOfDayStateEmpty ||
                      data.problemOfDayState is HomeFeatureProblemOfDayStateSolved

                    if shouldShowContinueInWebButton {
                        Button(
                            Strings.Home.continueLearningInWebButton,
                            action: viewModel.doContinueLearningOnWebPresentation
                        )
                        .buttonStyle(OutlineButtonStyle())
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
                displayStep(stepRoute: data.stepRoute)
            case .topicsRepetitionsScreen:
                let assembly = TopicsRepetitionsAssembly()
                stackRouter.pushViewController(assembly.makeModule())
            }
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            switch GamificationToolbarFeatureActionViewActionKs(gamificationToolbarViewAction.viewAction) {
            case .showProfileTab:
                TabBarRouter(tab: .profile).route()
            case .showProgressScreen:
                let assembly = ProgressScreenAssembly()
                stackRouter.pushViewController(assembly.makeModule())
            }
        case .problemsLimitViewAction:
            break
        case .nextLearningActivityWidgetViewAction(let nextLearningActivityWidgetViewAction):
            handleNextLearningActivityWidgetViewAction(
                NextLearningActivityWidgetFeatureActionViewActionKs(nextLearningActivityWidgetViewAction.viewAction)
            )
        }
    }

    #warning("ALTAPPS-909: Refactor this")
    private func handleNextLearningActivityWidgetViewAction(
        _ viewActionKs: NextLearningActivityWidgetFeatureActionViewActionKs
    ) {
        switch viewActionKs {
        case .navigateTo(let nextLearningActivityWidgetNavigateToViewAction):
            switch NextLearningActivityWidgetFeatureActionViewActionNavigateToKs(
                nextLearningActivityWidgetNavigateToViewAction
            ) {
            case .learningActivityTarget(let navigateToLearningActivityTargetViewAction):
                switch LearningActivityTargetViewActionKs(navigateToLearningActivityTargetViewAction.viewAction) {
                case .showStageImplementIDERequiredModal:
                    let panModal = StageImplementUnsupportedModalViewController(delegate: viewModel)
                    panModalPresenter.presentPanModal(panModal)
                case .navigateTo(let navigateToViewAction):
                    switch LearningActivityTargetViewActionNavigateToKs(navigateToViewAction) {
                    case .selectProject(let navigateToSelectProjectViewAction):
                        let assembly = ProjectSelectionListAssembly(
                            isNewUserMode: false,
                            trackID: navigateToSelectProjectViewAction.trackId
                        )
                        stackRouter.pushViewController(assembly.makeModule())
                    case .selectTrack:
                        let assembly = TrackSelectionListAssembly(isNewUserMode: false)
                        stackRouter.pushViewController(assembly.makeModule())
                    case .stageImplement(let navigateToStageImplementViewAction):
                        let assembly = StageImplementAssembly(
                            projectID: navigateToStageImplementViewAction.projectId,
                            stageID: navigateToStageImplementViewAction.stageId
                        )
                        stackRouter.pushViewController(assembly.makeModule())
                    case .step(let navigateToStepViewAction):
                        let assembly = StepAssembly(
                            stepRoute: navigateToStepViewAction.stepRoute
                        )
                        stackRouter.pushViewController(assembly.makeModule())
                    }
                }
            }
        }
    }

    private func displayStep(stepRoute: StepRoute) {
        let assembly = StepAssembly(stepRoute: stepRoute)
        stackRouter.pushViewController(assembly.makeModule())
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            HomeAssembly().makeModule()
        }
    }
}
