import shared
import SwiftUI

extension ProjectSelectionRootView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProjectSelectionRootView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProjectSelectionListViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationViewStyle(StackNavigationViewStyle())
        .navigationTitle(Strings.ProjectSelectionList.title)
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
        switch viewModel.viewStateKs {
        case .idle:
            ProjectSelectionSkeletonView()
                .onAppear(perform: viewModel.doLoadProjectSelectionList)
        case .loading:
            ProjectSelectionSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadProjectSelectionList
                )
            )
        case .content(let viewData):
            ProjectSelectionContentView(
                viewData: viewData,
                onProjectTap: viewModel.doMainProjectAction(projectID:)
            )
        }
    }
}

// MARK: - ProjectSelectionRootView (ViewAction) -

private extension ProjectSelectionRootView {
    func handleViewAction(_ viewAction: ProjectSelectionListFeatureActionViewAction) {
        switch ProjectSelectionListFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(ProjectSelectionListFeatureActionViewActionNavigateToKs(navigateToViewAction))
        case .showProjectSelectionConfirmationModal(let showProjectSelectionConfirmationModalViewAction):
            handleShowProjectSelectionConfirmationModalViewAction(showProjectSelectionConfirmationModalViewAction)
        case .showProjectSelectionStatus(let showProjectSelectionStatusViewAction):
            handleShowProjectSelectionStatusViewAction(
                ProjectSelectionListFeatureActionViewActionShowProjectSelectionStatusKs(
                    showProjectSelectionStatusViewAction
                )
            )
        }
    }

    func handleNavigateToViewAction(_ viewAction: ProjectSelectionListFeatureActionViewActionNavigateToKs) {
        switch viewAction {
        case .studyPlan:
            break // TODO: implement this
        }
    }

    func handleShowProjectSelectionConfirmationModalViewAction(
        _ viewAction: ProjectSelectionListFeatureActionViewActionShowProjectSelectionConfirmationModal
    ) {
        // TODO: implement this
    }

    func handleShowProjectSelectionStatusViewAction(
        _ viewAction: ProjectSelectionListFeatureActionViewActionShowProjectSelectionStatusKs
    ) {
        switch viewAction {
        case .error:
            break // TODO: implement this
        case .success:
            break // TODO: implement this
        }
    }
}

// MARK: - ProjectSelectionRootView_Previews: PreviewProvider -

struct ProjectSelectionRootView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            ProjectSelectionListAssembly(trackID: 1)
                .makeModule()
        }
    }
}
