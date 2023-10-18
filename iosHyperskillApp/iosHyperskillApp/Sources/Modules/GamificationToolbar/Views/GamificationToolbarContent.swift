import shared
import SwiftUI

extension GamificationToolbarContent {
    struct Appearance {
        let toolbarDefaultSkeletonSize = CGSize(width: 56, height: 28)
        let toolbarLargeSkeletonSize = CGSize(width: 100, height: 28)
    }
}

struct GamificationToolbarContent: ToolbarContent {
    private(set) var appearance = Appearance()

    let stateKs: GamificationToolbarFeatureStateKs

    let onGemsTap: () -> Void
    let onStreakTap: () -> Void
    let onProgressTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .primaryAction) {
            switch stateKs {
            case .idle, .loading:
                HStack {
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarLargeSkeletonSize))
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarDefaultSkeletonSize))
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarDefaultSkeletonSize))
                }
            case .error:
                HStack {}
            case .content(let data):
                HStack {
                    if let trackProgress = data.trackProgress {
                        ProgressBarButtonItem(
                            progress: Float(trackProgress.averageProgress) / 100,
                            isCompleted: trackProgress.isCompleted,
                            onTap: onProgressTap
                        )
                    }

                    StreakBarButtonItem(
                        currentStreak: Int(data.currentStreak),
                        isCompletedToday: data.historicalStreak.isCompleted,
                        onTap: onStreakTap
                    )

                    GemsBarButtonItem(
                        hypercoinsBalance: Int(data.hypercoinsBalance),
                        onTap: onGemsTap
                    )
                }
            }
        }
    }
}
