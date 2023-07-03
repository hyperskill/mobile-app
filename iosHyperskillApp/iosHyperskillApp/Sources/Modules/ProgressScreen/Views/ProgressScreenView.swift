import shared
import SwiftUI

extension ProgressScreenView {
    struct Appearance {
        let spacingBetweenSections = LayoutInsets.defaultInset * 2

        let cardRootSpacing = LayoutInsets.defaultInset
        let cardInteritemSpacing = LayoutInsets.smallInset

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProgressScreenView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProgressScreenViewModel

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
        ScrollView {
            VStack(spacing: appearance.spacingBetweenSections) {
                ProgressScreenTrackProgressView(
                    appearance: .init(
                        spacing: appearance.cardRootSpacing,
                        interitemSpacing: appearance.cardInteritemSpacing
                    ),
                    trackProgressViewStateKs: viewModel.trackProgressViewStateKs
                )

                ProgressScreenProjectProgressView(
                    appearance: .init(
                        spacing: appearance.cardRootSpacing,
                        interitemSpacing: appearance.cardInteritemSpacing
                    ),
                    projectProgressViewStateKs: viewModel.projectProgressViewStateKs
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
    ) {}
}
