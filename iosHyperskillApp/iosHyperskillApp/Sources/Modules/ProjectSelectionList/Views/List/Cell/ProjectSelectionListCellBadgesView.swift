import SwiftUI

struct ProjectSelectionListCellBadgesView: View {
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

struct ProjectSelectionListCellBadgesView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBestRated: true,
            isFastestToComplete: true
        )
        .padding()

        ProjectSelectionListCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBestRated: true,
            isFastestToComplete: true
        )
        .preferredColorScheme(.dark)
    }
}
