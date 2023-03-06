import shared
import SwiftUI

struct StageImplementView: View {
    @StateObject var viewModel: StageImplementViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
        }
        .navigationBarHidden(false)
        .navigationBarTitleDisplayMode(.inline)
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
        switch viewModel.stateKs {
        case .idle:
            ProgressView()
                .onAppear {
                    viewModel.doLoadStageImplement()
                }
        case .loading:
            ProgressView()
        case .deprecated(let data):
            PlaceholderView(
                configuration: .networkError(
                    titleText: data.message,
                    buttonText: data.ctaButtonText,
                    action: viewModel.doDeprecatedButtonClicked
                )
            )
        case .unsupported:
            PlaceholderView(configuration: .imageAndTitle(titleText: "Unsupported"))
        case .networkError:
            PlaceholderView(
                configuration: .networkError(
                    action: {
                        viewModel.doLoadStageImplement(forceUpdate: true)
                    }
                )
            )
        case .content(let data):
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Text(data.stageTitle)
                        .font(.headline)
                        .foregroundColor(.primaryText)
                        .padding([.horizontal, .top])

                    StepSwiftUIAssembly(
                        stepRoute: data.stepRoute,
                        rootViewController: stackRouter.rootViewController
                    )
                    .makeModule()
                }
            }
            .navigationTitle(data.navigationTitle)
        }
    }

    private func handleViewAction(_ viewAction: StageImplementFeatureActionViewAction) {
        switch StageImplementFeatureActionViewActionKs(viewAction) {
        case .showUnsupportedModal:
            #warning("showUnsupportedModal has not been implemented")
        case .navigateTo(let navigateToViewAction):
            switch StageImplementFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .back:
                stackRouter.popViewController()
            case .homeScreen:
                stackRouter.popToRootViewController()
                TabBarRouter(tab: .home).route()
            }
        }
    }
}

struct StageImplementView_Previews: PreviewProvider {
    static var previews: some View {
        //StageImplementView()
        Text("")
    }
}
