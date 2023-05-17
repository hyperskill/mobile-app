import shared
import SwiftUI

extension TrackSelectionListView {
    struct Appearance {
        let spacing: CGFloat = 32

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct TrackSelectionListView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: TrackSelectionListViewModel

    @ObservedObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationViewStyle(StackNavigationViewStyle())
        .navigationTitle(Strings.TrackSelectionList.title)
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
            TrackSelectionListSkeletonView()
                .onAppear(perform: viewModel.doLoadTrackSelectionList)
        case .loading:
            TrackSelectionListSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryLoadTrackSelectionList
                )
            )
        case .content(let viewData):
            ScrollView {
                VStack(spacing: appearance.spacing) {
                    TrackSelectionListHeaderView()

                    TrackSelectionListGridView(
                        tracks: viewData.tracks,
                        onTrackTap: viewModel.doMainTrackAction(trackID:)
                    )
                }
                .padding()
            }
            .frame(maxWidth: .infinity)
        }
    }
}

// MARK: - TrackSelectionListView (ViewAction) -

private extension TrackSelectionListView {
    func handleViewAction(_ viewAction: TrackSelectionListFeatureActionViewAction) {
        switch TrackSelectionListFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                navigateToViewAction
            )
        case .showTrackSelectionConfirmationModal(let showTrackSelectionConfirmationModalViewAction):
            handleShowTrackSelectionConfirmationModalViewAction(
                showTrackSelectionConfirmationModalViewAction
            )
        case .showTrackSelectionStatus(let showTrackSelectionStatusViewAction):
            handleShowTrackSelectionStatusViewAction(
                showTrackSelectionStatusViewAction
            )
        }
    }

    func handleNavigateToViewAction(_ viewAction: TrackSelectionListFeatureActionViewActionNavigateTo) {
        switch TrackSelectionListFeatureActionViewActionNavigateToKs(viewAction) {
        case .studyPlan:
            TabBarRouter(
                tab: .studyPlan,
                popToRoot: true
            )
            .route()
        }
    }

    func handleShowTrackSelectionConfirmationModalViewAction(
        _ viewAction: TrackSelectionListFeatureActionViewActionShowTrackSelectionConfirmationModal
    ) {
        guard let rootViewController = stackRouter.rootViewController else {
            return
        }

        let trackID = viewAction.track.id

        let alertController = UIAlertController(
            title: nil,
            message: "\(Strings.TrackSelectionList.title) \"\(viewAction.track.title)\"",
            preferredStyle: .alert
        )
        alertController.addAction(
            UIAlertAction(title: Strings.General.no, style: .cancel, handler: { [weak viewModel] _ in
                guard let viewModel else {
                    return
                }

                viewModel.doTrackSelectionConfirmationAction(trackID: trackID, isConfirmed: false)
                viewModel.logTrackSelectionConfirmationModalHiddenEvent()
            })
        )
        alertController.addAction(
            UIAlertAction(title: Strings.General.yes, style: .default, handler: { [weak viewModel] _ in
                guard let viewModel else {
                    return
                }

                viewModel.doTrackSelectionConfirmationAction(trackID: trackID, isConfirmed: true)
                viewModel.logTrackSelectionConfirmationModalHiddenEvent()
            })
        )

        rootViewController.present(
            alertController,
            animated: true,
            completion: { [weak viewModel] in
                viewModel?.logTrackSelectionConfirmationModalShownEvent()
            }
        )
    }

    func handleShowTrackSelectionStatusViewAction(
        _ viewAction: TrackSelectionListFeatureActionViewActionShowTrackSelectionStatus
    ) {
        switch TrackSelectionListFeatureActionViewActionShowTrackSelectionStatusKs(viewAction) {
        case .error:
            ProgressHUD.showError()
        case .loading:
            ProgressHUD.show()
        case .success:
            ProgressHUD.showSuccess()
        }
    }
}

// MARK: - TrackSelectionListView_Previews: PreviewProvider -

struct TrackSelectionListView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            TrackSelectionListAssembly()
                .makeModule()
        }
    }
}
