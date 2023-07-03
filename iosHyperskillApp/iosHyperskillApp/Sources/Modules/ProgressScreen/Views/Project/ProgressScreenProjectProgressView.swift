import shared
import SwiftUI

extension ProgressScreenProjectProgressView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat

        let cardBackgroundColor: Color
        let cardCornerRadius: CGFloat
    }
}

struct ProgressScreenProjectProgressView: View {
    let appearance: Appearance

    let projectProgressViewStateKs: ProgressScreenViewStateProjectProgressViewStateKs

    let onRetryTap: () -> Void

    var body: some View {
        switch projectProgressViewStateKs {
        case .idle, .loading:
            ProgressScreenProjectProgressSkeletonView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                )
            )
        case .empty:
            EmptyView()
        case .error:
            PlaceholderView(
                configuration: .reloadContent(
                    backgroundColor: appearance.cardBackgroundColor,
                    action: onRetryTap
                )
            )
            .cornerRadius(appearance.cardCornerRadius)
        case .content(let data):
            ProgressScreenProjectProgressContentView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing,
                    cardBackgroundColor: appearance.cardBackgroundColor,
                    cardCornerRadius: appearance.cardCornerRadius
                ),
                projectLevel: data.level.flatMap(SharedProjectLevelWrapper.init(sharedProjectLevel:)),
                title: data.title,
                timeToCompleteLabel: data.timeToCompleteLabel,
                completedStagesLabel: data.completedStagesLabel,
                completedStagesProgress: data.completedStagesProgress,
                isCompleted: data.isCompleted
            )
        }
    }
}
