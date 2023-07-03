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
                appliedTopicsCountLabel: data.appliedTopicsCountLabel,
                appliedTopicsPercentageLabel: data.appliedTopicsPercentageLabel,
                appliedTopicsPercentageProgress: data.appliedTopicsPercentageProgress,
                timeToCompleteLabel: data.timeToCompleteLabel,
                completedGraduateProjectsCount: Int(data.completedGraduateProjectsCount),
                isCompleted: data.isCompleted
            )
        }
    }
}
