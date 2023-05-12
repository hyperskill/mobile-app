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
        NavigationView {
            ZStack {
                UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

                BackgroundView(color: appearance.backgroundColor)

                buildBody()
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.ProjectSelectionList.title)
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
        switch viewModel.viewStateKs {
        case .idle:
            EmptyView()
        case .loading:
            EmptyView()
        case .error:
            EmptyView()
        case .content(let viewData):
            ProjectSelectionContentView(viewData: viewData)
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
        ProjectSelectionListAssembly(trackID: 1)
            .makeModule()
    }
}
