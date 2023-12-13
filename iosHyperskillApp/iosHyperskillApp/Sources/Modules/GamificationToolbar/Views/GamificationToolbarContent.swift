import shared
import SwiftUI

extension GamificationToolbarContent {
    struct Appearance {
        let skeletonSize = CGSize(width: 56, height: 28)
    }
}

struct GamificationToolbarContent: ToolbarContent {
    private(set) var appearance = Appearance()

    let viewStateKs: GamificationToolbarFeatureViewStateKs

    let onStreakTap: () -> Void
    let onProgressTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .primaryAction) {
            switch viewStateKs {
            case .idle, .loading:
                HStack {
                    SkeletonRoundedView(appearance: .init(size: appearance.skeletonSize))
                    SkeletonRoundedView(appearance: .init(size: appearance.skeletonSize))
                }
            case .error:
                HStack {}
            case .content(let data):
                HStack {
                    if let progress = data.progress {
                        ProgressBarButtonItem(
                            progress: progress.value,
                            formattedProgress: progress.formattedValue,
                            isCompleted: progress.isCompleted,
                            onTap: onProgressTap
                        )
                    }

                    StreakBarButtonItem(
                        currentStreak: data.streak.formattedValue,
                        isCompletedToday: data.streak.isCompleted,
                        onTap: onStreakTap
                    )
                }
            }
        }
    }
}
