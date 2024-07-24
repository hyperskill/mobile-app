import SwiftUI

struct ProjectSelectionListGridCellBadgesView: View {
    let isSelected: Bool
    let isIdeRequired: Bool
    let isBeta: Bool
    let isBestRated: Bool
    let isFastestToComplete: Bool

    private var isEmpty: Bool {
        !isSelected && !isIdeRequired && !isBeta && !isBestRated && !isFastestToComplete
    }

    var body: some View {
        if !isEmpty {
            FlowLayoutCompatibility(
                configuration: .init(
                    spacing: LayoutInsets.smallInset,
                    fallbackLayout: .horizontal()
                )
            ) {
                if isSelected {
                    BadgeView.selected()
                }

                if isIdeRequired {
                    BadgeView.ideRequired()
                }

                if isBeta {
                    BadgeView.beta()
                }

                if isBestRated {
                    BadgeView.bestRating()
                }

                if isFastestToComplete {
                    BadgeView.fastestToComplete()
                }
            }
        }
    }
}

#if DEBUG
#Preview {
    ProjectSelectionListGridCellBadgesView(
        isSelected: true,
        isIdeRequired: true,
        isBeta: true,
        isBestRated: true,
        isFastestToComplete: true
    )
}

#Preview {
    ProjectSelectionListGridCellBadgesView(
        isSelected: true,
        isIdeRequired: true,
        isBeta: true,
        isBestRated: true,
        isFastestToComplete: true
    )
    .preferredColorScheme(.dark)
}
#endif
