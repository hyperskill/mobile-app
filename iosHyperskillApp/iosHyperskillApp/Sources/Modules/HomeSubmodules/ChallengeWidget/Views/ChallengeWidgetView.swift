import shared
import SwiftUI

extension ChallengeWidgetView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.surface)
        let skeletonHeight: CGFloat = 128
    }
}

struct ChallengeWidgetView: View {
    private(set) var appearance = Appearance()

    let viewStateKs: ChallengeWidgetViewStateKs

    let viewModel: ChallengeWidgetViewModel

    var body: some View {
        buildBody()
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewStateKs {
        case .idle, .empty:
            EmptyView()
        case .loading(let data):
            if data.shouldShowSkeleton {
                SkeletonRoundedView()
                    .frame(height: appearance.skeletonHeight)
            } else {
                EmptyView()
            }
        case .error:
            ChallengeWidgetErrorView(
                backgroundColor: appearance.backgroundColor,
                action: viewModel.doRetryContentLoading
            )
        case .content(let contentState):
            ChallengeWidgetContentStateView(
                appearance: .init(backgroundColor: appearance.backgroundColor),
                stateKs: .init(contentState),
                onOpenDescriptionLink: viewModel.doOpenDescriptionLink(_:),
                onDeadlineReloadTap: viewModel.doDeadlineReloadAction,
                onCollectRewardTap: viewModel.doCollectReward
            )
            .equatable()
        }
    }
}

extension ChallengeWidgetView: Equatable {
    static func == (lhs: ChallengeWidgetView, rhs: ChallengeWidgetView) -> Bool {
        lhs.viewStateKs == rhs.viewStateKs
    }
}

#Preview("Error") {
    ChallengeWidgetView(
        viewStateKs: .error,
        viewModel: ChallengeWidgetViewModel()
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
