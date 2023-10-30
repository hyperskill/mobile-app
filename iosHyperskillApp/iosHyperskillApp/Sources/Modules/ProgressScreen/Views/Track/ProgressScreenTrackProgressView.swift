import shared
import SwiftUI

extension ProgressScreenTrackProgressView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat

        let cardBackgroundColor: Color
        let cardCornerRadius: CGFloat
    }
}

struct ProgressScreenTrackProgressView: View {
    let appearance: Appearance

    let trackProgressViewStateKs: ProgressScreenViewStateTrackProgressViewStateKs

    let onRetryTap: () -> Void

    let onChangeTrackTap: () -> Void

    var body: some View {
        switch trackProgressViewStateKs {
        case .idle, .loading:
            ProgressScreenTrackProgressSkeletonView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing
                )
            )
        case .error:
            PlaceholderView(
                configuration: .reloadContent(
                    backgroundColor: appearance.cardBackgroundColor,
                    action: onRetryTap
                )
            )
            .cornerRadius(appearance.cardCornerRadius)
        case .content(let data):
            let appliedTopicsContentState =
              data.appliedTopicsState as? ProgressScreenViewStateTrackProgressViewStateContentAppliedTopicsStateContent

            ProgressScreenTrackProgressContentView(
                appearance: .init(
                    spacing: appearance.spacing,
                    interitemSpacing: appearance.interitemSpacing,
                    cardBackgroundColor: appearance.cardBackgroundColor,
                    cardCornerRadius: appearance.cardCornerRadius
                ),
                avatarImageSource: data.imageSource,
                title: data.title,
                completedTopicsCountLabel: data.completedTopicsCountLabel,
                completedTopicsPercentageLabel: data.completedTopicsPercentageLabel,
                completedTopicsPercentageProgress: data.completedTopicsPercentageProgress,
                appliedTopicsCountLabel: appliedTopicsContentState?.countLabel,
                appliedTopicsPercentageLabel: appliedTopicsContentState?.percentageLabel,
                appliedTopicsPercentageProgress: appliedTopicsContentState?.percentageProgress,
                timeToCompleteLabel: data.timeToCompleteLabel,
                completedGraduateProjectsCount: data.completedGraduateProjectsCount?.intValue,
                isCompleted: data.isCompleted,
                onChangeTrackTap: onChangeTrackTap
            )
        }
    }
}
