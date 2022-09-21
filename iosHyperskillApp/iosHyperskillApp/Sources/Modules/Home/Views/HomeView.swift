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

    var body: some View {
        NavigationView {
            ZStack {
                UIViewControllerEventsWrapper(
                    onViewDidAppear: {
                        viewModel.logViewedEvent()
                        viewModel.loadContent()
                    }
                )

                BackgroundView(color: appearance.backgroundColor)

                buildBody()
            }
            .navigationTitle(Strings.Home.title)
            .navigationBarHidden(true)
        }
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
                    viewModel.loadContent()
                }
        case is HomeFeatureStateLoading:
            ProgressView()
        case is HomeFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.loadContent(forceUpdate: true)
                }
            )
        case let data as HomeFeatureStateContent:
            ScrollView {
                VStack(alignment: .leading, spacing: appearance.spacingBetweenContainers) {
                    Text(Strings.Home.helloLetsLearn)
                        .font(.title)
                        .foregroundColor(.primaryText)

                    Text(Strings.Home.keepPracticing)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    if let streak = data.streak {
                        StreakViewBuilder(streak: streak, viewType: .card).build()
                    }

                    ProblemOfDayAssembly(
                        problemOfDayState: data.problemOfDayState,
                        output: viewModel
                    )
                    .makeModule()

                    let shouldShowContinueInWebButton = data.problemOfDayState is HomeFeatureProblemOfDayStateEmpty ||
                      data.problemOfDayState is HomeFeatureProblemOfDayStateSolved

                    if shouldShowContinueInWebButton {
                        OpenURLInsideAppButton(
                            text: Strings.Track.continueInWebButton,
                            url: HyperskillURLFactory.makeIndex().require(),
                            webControllerType: .custom()
                        )
                        .buttonStyle(OutlineButtonStyle())
                    }

                    HomeDebugStepNavigationView()
                }
                .padding()
            }
            .frame(maxWidth: .infinity)
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: HomeFeatureActionViewAction) {
        print("HomeView :: \(#function) viewAction = \(viewAction)")
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeAssembly().makeModule()
    }
}
