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
            ProjectSelectionListSkeletonView()
                .onAppear(perform: viewModel.doLoadProjectSelectionList)
        case .loading:
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
                    .padding(.top, appearance.spacing)

                    ProjectSelectionListGridView(
                        viewData: viewData,
                        onProjectTap: viewModel.doMainProjectAction(projectID:)
                    )
                }
                .padding([.horizontal, .bottom])
            }
            .frame(maxWidth: .infinity)
        }
    }
}

// MARK: - ProjectSelectionRootView (ViewAction) -

private extension ProjectSelectionListView {
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
            TabBarRouter(tab: .studyPlan, popToRoot: true).route()
        }
    }

    func handleShowProjectSelectionConfirmationModalViewAction(
        _ viewAction: ProjectSelectionListFeatureActionViewActionShowProjectSelectionConfirmationModal
    ) {
        guard let rootViewController = stackRouter.rootViewController else {
            return
        }

        let projectID = viewAction.project.id

        let alertController = UIAlertController(
            title: nil,
            message: "\(Strings.ProjectSelectionList.title) \"\(viewAction.project.title)\"",
            preferredStyle: .alert
        )
        alertController.addAction(
            UIAlertAction(title: Strings.General.no, style: .cancel, handler: { [weak viewModel] _ in
                guard let viewModel else {
                    return
                }

                viewModel.doProjectSelectionConfirmationAction(projectID: projectID, isConfirmed: false)
                viewModel.logProjectSelectionConfirmationModalHiddenEvent()
            })
        )
        alertController.addAction(
            UIAlertAction(title: Strings.General.yes, style: .default, handler: { [weak viewModel] _ in
                guard let viewModel else {
                    return
                }

                ProgressHUD.show()

                viewModel.doProjectSelectionConfirmationAction(projectID: projectID, isConfirmed: true)
                viewModel.logProjectSelectionConfirmationModalHiddenEvent()
            })
        )

        rootViewController.present(
            alertController,
            animated: true,
            completion: { [weak viewModel] in
                viewModel?.logProjectSelectionConfirmationModalShownEvent()
            }
        )
    }

    func handleShowProjectSelectionStatusViewAction(
        _ viewAction: ProjectSelectionListFeatureActionViewActionShowProjectSelectionStatusKs
    ) {
        switch viewAction {
        case .error:
            ProgressHUD.showError()
        case .success:
            ProgressHUD.showSuccess()
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
