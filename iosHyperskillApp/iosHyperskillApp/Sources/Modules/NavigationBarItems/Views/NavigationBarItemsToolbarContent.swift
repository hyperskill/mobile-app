import shared
import SwiftUI

extension NavigationBarItemsToolbarContent {
    struct Appearance {
        let toolbarSkeletonSize = CGSize(width: 56, height: 28)
    }
}

struct NavigationBarItemsToolbarContent: ToolbarContent {
    private(set) var appearance = Appearance()

    let navigationBarItemsStateKs: NavigationBarItemsFeatureStateKs

    let onGemsTap: () -> Void
    let onStreakTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .primaryAction) {
            switch navigationBarItemsStateKs {
            case .idle, .loading:
                HStack {
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
                }
            case .error:
                HStack {}
            case .content(let data):
                HStack {
                    if let streak = data.streak {
                        StreakBarButtonItem(
                            currentStreak: Int(streak.currentStreak),
                            isCompletedToday: streak.history.first?.isCompleted == true,
                            onTap: onStreakTap
                        )
                    }

                    GemsBarButtonItem(
                        hypercoinsBalance: Int(data.hypercoinsBalance),
                        onTap: onGemsTap
                    )
                }
            }
        }
    }
}
