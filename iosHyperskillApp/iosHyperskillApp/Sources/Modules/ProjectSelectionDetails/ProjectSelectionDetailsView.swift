import shared
import SwiftUI

extension ProjectSelectionDetailsView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProjectSelectionDetailsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProjectSelectionDetailsViewModel

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
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadProjectSelectionDetails
                )
            )
        case .content(let viewData):
            Text("Hello, World!")
        }
    }
}

// MARK: - ProjectSelectionDetailsView (ViewAction) -

private extension ProjectSelectionDetailsView {
    func handleViewAction(
        _ viewAction: ProjectSelectionDetailsFeatureActionViewAction
    ) {
        switch ProjectSelectionDetailsFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        case .showProjectSelectionStatus(let showProjectSelectionStatusViewAction):
            handleShowProjectSelectionStatusViewAction(showProjectSelectionStatusViewAction)
        }
    }

    func handleNavigateToViewAction(
        _ viewAction: ProjectSelectionDetailsFeatureActionViewActionNavigateTo
    ) {
        switch ProjectSelectionDetailsFeatureActionViewActionNavigateToKs(viewAction) {
        case .studyPlan:
            TabBarRouter(
                tab: .studyPlan,
                popToRoot: true
            )
            .route()
        }
    }

    func handleShowProjectSelectionStatusViewAction(
        _ viewAction: ProjectSelectionDetailsFeatureActionViewActionShowProjectSelectionStatus
    ) {
        #warning("shared resources")
        switch ProjectSelectionDetailsFeatureActionViewActionShowProjectSelectionStatusKs(viewAction) {
        case .error:
            ProgressHUD.showError(status: nil)
        case .success:
            ProgressHUD.showSuccess(status: nil)
        }
    }
}
