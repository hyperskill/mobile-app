import shared
import SwiftUI

extension ProgressScreenView {
    struct Appearance {
        let spacingBetweenSections = LayoutInsets.defaultInset * 2

        let cardRootSpacing = LayoutInsets.defaultInset
        let cardInteritemSpacing = LayoutInsets.smallInset
        let cardBackgroundColor = Color(ColorPalette.surface)
        let cardCornerRadius: CGFloat = 8

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProgressScreenView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProgressScreenViewModel

    @ObservedObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            if viewModel.state.isInErrorState {
                PlaceholderView(
                    configuration: .networkError(
                        backgroundColor: appearance.backgroundColor,
                        action: viewModel.doRetryLoadProgressScreen
                    )
                )
            } else {
                buildContent()
            }
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
    private func buildContent() -> some View {
        ScrollView {
            VStack(spacing: appearance.spacingBetweenSections) {
                ProgressScreenTrackProgressView(
                    appearance: .init(
                        spacing: appearance.cardRootSpacing,
                        interitemSpacing: appearance.cardInteritemSpacing,
                        cardBackgroundColor: appearance.cardBackgroundColor,
                        cardCornerRadius: appearance.cardCornerRadius
                    ),
                    trackProgressViewStateKs: viewModel.trackProgressViewStateKs,
                    onRetryTap: viewModel.doRetryLoadTrackProgress,
                    onChangeTrackTap: viewModel.doTrackSelectionPresentation
                )

                ProgressScreenProjectProgressView(
                    appearance: .init(
                        spacing: appearance.cardRootSpacing,
                        interitemSpacing: appearance.cardInteritemSpacing,
                        cardBackgroundColor: appearance.cardBackgroundColor,
                        cardCornerRadius: appearance.cardCornerRadius
                    ),
                    projectProgressViewStateKs: viewModel.projectProgressViewStateKs,
                    onRetryTap: viewModel.doRetryLoadProjectProgress,
                    onChangeProjectTap: viewModel.doProjectSelectionPresentation
                )
            }
            .padding()
            .pullToRefresh(
                isShowing: Binding(
                    get: { viewModel.state.isRefreshing },
                    set: { _ in }
                ),
                onRefresh: viewModel.doPullToRefresh
            )
        }
        .frame(maxWidth: .infinity)
    }
}

 // MARK: - ProgressScreenView (ViewAction) -

private extension ProgressScreenView {
    func handleViewAction(
        _ viewAction: ProgressScreenFeatureActionViewAction
    ) {
        switch ProgressScreenFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch ProgressScreenFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .projectSelectionScreen(let navigateToSelectProjectViewAction):
                let assembly = ProjectSelectionListAssembly(
                    isNewUserMode: false,
                    trackID: navigateToSelectProjectViewAction.trackId
                )
                stackRouter.pushViewController(assembly.makeModule())
            case .trackSelectionScreen:
                let assembly = TrackSelectionListAssembly(isNewUserMode: false)
                stackRouter.pushViewController(assembly.makeModule())
            }
        }
    }
}
