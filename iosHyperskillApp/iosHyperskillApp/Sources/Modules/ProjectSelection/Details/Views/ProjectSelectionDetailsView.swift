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

    @ObservedObject var stackRouter: SwiftUIStackRouter

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
            ProjectSelectionDetailsSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadProjectSelectionDetails
                )
            )
        case .content(let viewData):
            let _ = handleProjectSelectionLoadingIndicatorVisibility(
                isVisible: viewData.isSelectProjectLoadingShowed
            )

            ProjectSelectionDetailsContentView(
                navigationTitle: viewData.formattedTitle,
                learningOutcomesDescription: viewData.learningOutcomesDescription,
                isSelected: viewData.isSelected,
                isIdeRequired: viewData.isIdeRequired,
                isBeta: viewData.isBeta,
                isBestRated: viewData.isBestRated,
                isFastestToComplete: viewData.isFastestToComplete,
                isBadgesVisible: viewData.areTagsVisible,
                averageRatingTitle: viewData.formattedAverageRating,
                projectLevel: viewData.projectLevel.flatMap(SharedProjectLevelWrapper.init(sharedProjectLevel:)),
                projectLevelTitle: viewData.formattedProjectLevel,
                graduateTitle: viewData.formattedGraduateDescription,
                timeToCompleteTitle: viewData.formattedTimeToComplete,
                providerName: viewData.providerName,
                isCallToActionButtonEnabled: viewData.isSelectProjectButtonEnabled,
                onCallToActionButtonTap: viewModel.doSelectProjectButtonClicked
            )
        }
    }

    private func handleProjectSelectionLoadingIndicatorVisibility(isVisible: Bool) {
        if isVisible {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismissWithDelay()
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
        case .studyPlan(let navigateToStudyPlanViewAction):
            handleNavigateToStudyPlanViewAction(navigateToStudyPlanViewAction)
        }
    }

    func handleNavigateToStudyPlanViewAction(
        _ viewAction: ProjectSelectionDetailsFeatureActionViewActionNavigateToStudyPlan
    ) {
        switch viewAction.command {
        case .backto:
            TabBarRouter(
                tab: .studyPlan,
                popToRoot: true
            )
            .route()

            DispatchQueue.main.async {
                stackRouter.popToRootViewController()
            }
        case .newrootscreen:
            viewModel.doNavigateToHomeAsNewRootScreenPresentation()
        default:
            assertionFailure("Did receive unexpected command: \(viewAction.command)")
        }
    }

    func handleShowProjectSelectionStatusViewAction(
        _ viewAction: ProjectSelectionDetailsFeatureActionViewActionShowProjectSelectionStatus
    ) {
        switch ProjectSelectionDetailsFeatureActionViewActionShowProjectSelectionStatusKs(viewAction) {
        case .error:
            ProgressHUD.showError(status: Strings.ProjectSelectionDetails.selectionErrorMessage)
        case .success:
            ProgressHUD.showSuccess(status: Strings.ProjectSelectionDetails.selectionSuccessMessage)
        }
    }
}
