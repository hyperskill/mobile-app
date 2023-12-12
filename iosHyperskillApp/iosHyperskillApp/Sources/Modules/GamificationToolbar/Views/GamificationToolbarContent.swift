import shared
import SwiftUI

extension GamificationToolbarContent {
    struct Appearance {
        let skeletonSize = CGSize(width: 56, height: 28)

        let searchImageWidthHeight: CGFloat = 16
        let searchImagePadding: CGFloat = 6
        let searchImageBackgroundColor = Color(ColorPalette.surface)
    }
}

struct GamificationToolbarContent: ToolbarContent {
    private(set) var appearance = Appearance()

    let viewStateKs: GamificationToolbarFeatureViewStateKs

    let onStreakTap: () -> Void
    let onProgressTap: () -> Void
    let onSearchTap: () -> Void

    var body: some ToolbarContent {
        ToolbarItem(placement: .primaryAction) {
            switch viewStateKs {
            case .idle, .loading:
                HStack {
                    SkeletonRoundedView(appearance: .init(size: appearance.skeletonSize))
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

                    Button(
                        action: onSearchTap,
                        label: {
                            Image(systemName: "magnifyingglass")
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .frame(widthHeight: appearance.searchImageWidthHeight)
                                .padding(appearance.searchImagePadding)
                                .background(Circle().foregroundColor(appearance.searchImageBackgroundColor))
                        }
                    )
                }
            }
        }
    }
}
