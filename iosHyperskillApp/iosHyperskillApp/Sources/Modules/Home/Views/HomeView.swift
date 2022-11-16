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
        .navigationBarHidden(true)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is HomeFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case is HomeFeatureStateLoading:
            ProgressView()
        case is HomeFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadContent(forceUpdate: true)
                }
            )
        case let state as HomeFeatureStateContent:
            ScrollView {
                VStack(alignment: .leading, spacing: appearance.spacingBetweenContainers) {
                    Text(Strings.Home.helloLetsLearn)
                        .font(.title)
                        .foregroundColor(.primaryText)

                    Text(Strings.Home.keepPracticing)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    if let streak = state.streak {
                        StreakViewBuilder(streak: streak, viewType: .card).build()
                    }

                    ProblemOfDayAssembly(
                        problemOfDayState: state.problemOfDayState,
                        output: viewModel
                    )
                    .makeModule()

                    let shouldShowContinueInWebButton = state.problemOfDayState is HomeFeatureProblemOfDayStateEmpty ||
                      state.problemOfDayState is HomeFeatureProblemOfDayStateSolved

                    if shouldShowContinueInWebButton {
                        OpenURLInsideAppButton(
                            text: Strings.Track.continueInWebButton,
                            urlType: .nextURLPath(HyperskillUrlPath.Index()),
                            webControllerType: .safari,
                            onTap: viewModel.logClickedContinueLearningOnWebEvent
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
                        get: { state.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
            .padding(.top, 0.1)
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
        switch viewAction {
        case let navigateToStepScreenViewAction as HomeFeatureActionViewActionNavigateToStepScreen:
            let assembly = StepAssembly(stepID: Int(navigateToStepScreenViewAction.stepId))
            pushRouter.pushViewController(assembly.makeModule())
        default:
            print("HomeView :: unhandled viewAction = \(viewAction) ")
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
