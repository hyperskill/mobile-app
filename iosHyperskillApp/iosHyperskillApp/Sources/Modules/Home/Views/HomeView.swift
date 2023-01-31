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
                onStreakTap: viewModel.doStreakBarButtonItemAction
            )
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
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
                    Text(Strings.Home.keepPracticing)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    ProblemOfDayAssembly(
                        problemOfDayState: data.problemOfDayState,
                        output: viewModel
                    )
                    .makeModule()

                    if let availableRepetitionsState = data.repetitionsState as? HomeFeatureRepetitionsStateAvailable {
                        TopicsRepetitionsCardView(
                            topicsToRepeatCount: Int(availableRepetitionsState.recommendedRepetitionsCount),
                            onTap: viewModel.doTopicsRepetitionsPresentation
                        )
                    }

                    let shouldShowContinueInWebButton = data.problemOfDayState is HomeFeatureProblemOfDayStateEmpty ||
                      data.problemOfDayState is HomeFeatureProblemOfDayStateSolved

                    if shouldShowContinueInWebButton {
                        Button(
                            Strings.Track.About.continueInWebButton,
                            action: viewModel.doContinueLearningOnWebPresentation
                        )
                        .buttonStyle(OutlineButtonStyle())
                    }

                    #if BETA_PROFILE || DEBUG
                    HomeDebugStepNavigationView(
                        onOpenStepTapped: { stepID in
                            let assembly = StepAssembly(stepRoute: StepRouteLearn(stepId: Int64(stepID)))
                            stackRouter.pushViewController(assembly.makeModule())
                        }
                    )
                    #endif
                }
                .padding()
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
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            switch GamificationToolbarFeatureActionViewActionKs(gamificationToolbarViewAction.viewAction) {
            case .showProfileTab:
                TabBarRouter(tab: .profile).route()
            }
        }
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            HomeAssembly().makeModule()
        }
    }
}
