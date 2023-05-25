import shared
import SwiftUI

extension TrackSelectionDetailsView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
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
        .navigationViewStyle(StackNavigationViewStyle())
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
            TrackSelectionDetailsContentView(
                navigationTitle: viewData.title,
                description: viewData.description_,
                isBeta: viewData.isBeta,
                isCompleted: viewData.isCompleted,
                isSelected: viewData.isSelected,
                rating: viewData.formattedRating,
                timeToComplete: viewData.formattedTimeToComplete,
                topicsCount: viewData.formattedTopicsCount,
                projectsCount: viewData.formattedProjectsCount,
                isCertificateAvailable: viewData.isCertificateAvailable,
                mainProviderTitle: viewData.mainProvider?.title,
                mainProviderDescription: viewData.mainProvider?.description_,
                otherProvidersDescription: viewData.formattedOtherProviders
            )
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
