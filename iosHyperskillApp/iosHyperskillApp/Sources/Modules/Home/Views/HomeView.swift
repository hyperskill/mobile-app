import shared
import SwiftUI

extension HomeView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset

        let toolbarSkeletonSize = CGSize(width: 56, height: 28)

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct HomeView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: HomeViewModel

    @StateObject var pushRouter: SwiftUIPushRouter

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
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    private var skeletons: some View {
        VStack(spacing: appearance.spacingBetweenContainers) {
            ProblemOfDaySkeletonView()
            TopicsRepetitionsCardSkeletonView()

            Spacer()
        }
        .toolbar {
            ToolbarItemGroup(placement: .primaryAction) {
                SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
                SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
            }
        }
        .padding()
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            skeletons
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            skeletons
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

                    TopicsRepetitionsCardView(
                        topicsToRepeatCount: Int(data.recommendedRepetitionsCount),
                        onTap: viewModel.doTopicsRepetitionsPresentation
                    )

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
                            pushRouter.pushViewController(StepAssembly(stepID: stepID).makeModule())
                        }
                    )
                    #endif
                }
                .padding()
                .pullToRefresh(
                    isShowing: Binding(
                        get: { data.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
            .toolbar {
                ToolbarItemGroup(placement: .primaryAction) {
                    if let streak = data.streak {
                        StreakBarButtonItem(
                            currentStreak: Int(streak.currentStreak),
                            onTap: viewModel.doStreakBarButtonItemAction
                        )
                    }

                    GemsBarButtonItem(
                        hypercoinsBalance: Int(data.hypercoinsBalance),
                        onTap: viewModel.doGemsBarButtonItemAction
                    )
                }
            }
        }
    }

    private func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
        switch HomeFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch HomeFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .stepScreen(let data):
                let assembly = StepAssembly(stepID: Int(data.stepId))
                pushRouter.pushViewController(assembly.makeModule())
            case .topicsRepetitionsScreen(let data):
                let assembly = TopicsRepetitionsAssembly(
                    recommendedRepetitionsCount: data.recommendedRepetitionsCount
                )
                pushRouter.pushViewController(assembly.makeModule())
            case .profile:
                TabBarRouter(tab: .profile).route()
            }
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
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
