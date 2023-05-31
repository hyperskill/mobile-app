import shared
import SwiftUI

extension ProjectSelectionListView {
    struct Appearance {
        let spacing: CGFloat = 32

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProjectSelectionListView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProjectSelectionListViewModel

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
        switch viewModel.viewStateKs {
        case .idle, .loading:
            ProjectSelectionListSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadProjectSelectionList
                )
            )
        case .content(let viewData):
            ScrollView {
                VStack(spacing: appearance.spacing) {
                    ProjectSelectionListHeaderView(
                        avatarSource: viewData.trackIcon,
                        title: viewData.formattedTitle
                    )

                    ProjectSelectionListGridView(
                        viewData: viewData,
                        onProjectTap: viewModel.doMainProjectAction(projectID:)
                    )
                }
                .padding()
            }
            .frame(maxWidth: .infinity)
        }
    }
}

// MARK: - ProjectSelectionListView (ViewAction) -

private extension ProjectSelectionListView {
    func handleViewAction(_ viewAction: ProjectSelectionListFeatureActionViewAction) {
        switch ProjectSelectionListFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                ProjectSelectionListFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        case .showProjectSelectionError:
            ProgressHUD.showError()
        }
    }

    func handleNavigateToViewAction(_ viewAction: ProjectSelectionListFeatureActionViewActionNavigateToKs) {
        switch viewAction {
        case .projectDetails(let navigateToProjectDetailsViewAction):
            let assembly = ProjectSelectionDetailsAssembly(
                isNewUserMode: navigateToProjectDetailsViewAction.isNewUserMode,
                trackId: navigateToProjectDetailsViewAction.trackId,
                projectId: navigateToProjectDetailsViewAction.projectId,
                isProjectSelected: navigateToProjectDetailsViewAction.isProjectSelected,
                isProjectBestRated: navigateToProjectDetailsViewAction.isProjectBestRated,
                isProjectFastestToComplete: navigateToProjectDetailsViewAction.isProjectFastestToComplete
            )
            stackRouter.pushViewController(assembly.makeModule())
        }
    }
}

// MARK: - ProjectSelectionRootView_Previews: PreviewProvider -

struct ProjectSelectionListView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            ProjectSelectionListAssembly(trackID: 1, isNewUserMode: false)
                .makeModule()
        }
    }
}
