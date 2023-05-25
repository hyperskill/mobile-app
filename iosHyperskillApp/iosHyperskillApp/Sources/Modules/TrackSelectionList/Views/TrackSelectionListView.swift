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
        case .showTrackSelectionError:
            ProgressHUD.showError()
        }
    }

    func handleNavigateToViewAction(_ viewAction: TrackSelectionListFeatureActionViewActionNavigateTo) {
        switch TrackSelectionListFeatureActionViewActionNavigateToKs(viewAction) {
        case .trackDetails(let navigateToTrackDetailsViewAction):
            let assembly = TrackSelectionDetailsAssembly(
                trackWithProgress: navigateToTrackDetailsViewAction.trackWithProgress,
                isTrackSelected: navigateToTrackDetailsViewAction.isTrackSelected
            )
            stackRouter.pushViewController(assembly.makeModule())
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
