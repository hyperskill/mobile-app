import shared
import SwiftUI

extension TrackSelectionDetailsView {
    struct Appearance {
        let backgroundColor = Color.background
    }
}

struct TrackSelectionDetailsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: TrackSelectionDetailsViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
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
        switch viewModel.stateKs {
        case .idle, .loading:
            ProgressView()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadTrackSelectionDetails
                )
            )
        case .content(let viewData):
            Text("Hello, World!")
        }
    }
}

// MARK: - TrackSelectionDetailsView (ViewAction) -

private extension TrackSelectionDetailsView {
    func handleViewAction(
        _ viewAction: TrackSelectionDetailsFeatureActionViewAction
    ) {
        switch TrackSelectionDetailsFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        case .showTrackSelectionStatus(let showTrackSelectionStatusViewAction):
            print(showTrackSelectionStatusViewAction)
        }
    }

    func handleNavigateToViewAction(_ viewAction: TrackSelectionDetailsFeatureActionViewActionNavigateTo) {
        switch TrackSelectionDetailsFeatureActionViewActionNavigateToKs(viewAction) {
        case .studyPlan:
            TabBarRouter(
                tab: .studyPlan,
                popToRoot: true
            )
            .route()
        }
    }

    func handleShowTrackSelectionStatusViewAction(
        _ viewAction: TrackSelectionDetailsFeatureActionViewActionShowTrackSelectionStatus
    ) {
        #warning("Use messages from shared module")
        switch TrackSelectionDetailsFeatureActionViewActionShowTrackSelectionStatusKs(viewAction) {
        case .error:
            ProgressHUD.showError()
        case .success:
            ProgressHUD.showSuccess()
        }
    }
}

// MARK: - TrackSelectionDetailsView_Previews: PreviewProvider -

#if DEBUG
struct TrackSelectionDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            TrackSelectionDetailsAssembly
                .makePlaceholder()
                .makeModule()
        }
    }
}
#endif
