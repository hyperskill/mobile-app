import SwiftUI

struct ProjectSelectionListGridCellBadgesView: View {
    let isSelected: Bool
    let isIdeRequired: Bool
    let isBestRated: Bool
    let isFastestToComplete: Bool

    private var isEmpty: Bool {
        !isSelected && !isIdeRequired && !isBestRated && !isFastestToComplete
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: LayoutInsets.smallInset) {
                if isSelected {
                    BadgeView.projectSelectionListSelected()
                }

                if isIdeRequired {
                    BadgeView.ideRequired()
                }

                if isBestRated {
                    BadgeView.projectSelectionListBestRating()
                }

                if isFastestToComplete {
                    BadgeView.projectSelectionListFastestToComplete()
                }
            }
        }
    }
}

struct ProjectSelectionListGridCellBadgesView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListGridCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBestRated: true,
            isFastestToComplete: true
        )
        .padding()

        ProjectSelectionListGridCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBestRated: true,
            isFastestToComplete: true
        )
        .preferredColorScheme(.dark)
    }
}
