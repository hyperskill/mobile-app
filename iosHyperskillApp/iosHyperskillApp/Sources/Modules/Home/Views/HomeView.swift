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

    @ObservedObject private var viewModel: HomeViewModel

    init(viewModel: HomeViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            ZStack {
                BackgroundView(color: appearance.backgroundColor)
                buildBody()
            }
            .navigationTitle(Strings.Home.title)
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
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

                    #if DEBUG
                    HomeDebugStepNavigationView()
                    #endif
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
