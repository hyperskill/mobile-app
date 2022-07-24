import shared
import SwiftUI

extension HomeView {
    struct Appearance {
        let spacing: CGFloat = 20
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
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.Home.helloLetsLearn)
                .font(.title)
                .foregroundColor(.primaryText)

            Text(Strings.Home.keepPracticing)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            buildBody()

            Spacer()
        }
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
        .padding()
        .background(Color(ColorPalette.background).ignoresSafeArea())
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
                .frame(maxWidth: .infinity, alignment: .center)
        case is HomeFeatureStateLoading:
            ProgressView()
                .frame(maxWidth: .infinity, alignment: .center)
        case is HomeFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: Color(ColorPalette.background)) {
                    viewModel.loadContent(forceUpdate: true)
                }
            )
        case let data as HomeFeatureStateContent:
            buildContent(data: data)
        default:
            Text("Unkwown state")
        }
    }

    @ViewBuilder
    private func buildContent(data: HomeFeatureStateContent) -> some View {
        VStack(spacing: appearance.spacing) {
            if let streak = data.streak {
                StreakViewBuilder(streak: streak, viewType: .card).build()
            }

            if let viewData = viewModel.makeProblemOfDayViewData() {
                ProblemOfDayCardView(
                    viewData: viewData,
                    onReloadTap: { viewModel.loadContent(forceUpdate: true) }
                )
            }
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
