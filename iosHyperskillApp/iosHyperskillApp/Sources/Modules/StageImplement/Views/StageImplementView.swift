import shared
import SwiftUI

struct StageImplementView: View {
    @StateObject var viewModel: StageImplementViewModel

    @ObservedObject var stackRouter: SwiftUIStackRouter

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
        case .idle, .loading:
            ProgressView()
        case .networkError:
            PlaceholderView(configuration: .networkError(action: viewModel.doRetryLoadStageImplement))
        case .content(let data):
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Text(data.stageTitle)
                        .font(.headline)
                        .foregroundColor(.primaryText)
                        .padding([.horizontal, .top])

                    StepAssembly.stageImplement(
                        stepRoute: data.stepRoute,
                        rootViewController: stackRouter.rootViewController
                    )
                    .makeModule()
                }
            }
            .navigationTitle(data.navigationTitle)
        }
    }

    private func handleViewAction(_ viewAction: StageImplementFeatureActionViewAction) {}
}

struct StageImplementView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StageImplementAssembly(
                projectID: 71,
                stageID: 390
            ).makeModule()
        }
    }
}
