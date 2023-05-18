import SwiftUI

struct TrackSelectionListGridCellBadgesView: View {
    let isSelected: Bool
    let isIdeRequired: Bool
    let isBeta: Bool
    let isCompleted: Bool

    var isEmpty: Bool {
        !isSelected && !isIdeRequired && !isBeta && !isCompleted
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: LayoutInsets.smallInset) {
                if isSelected {
                    BadgeView.selected()
                }

                if isIdeRequired {
                    BadgeView.ideRequired()
                }

                if isBeta {
                    BadgeView.beta()
                }

                if isCompleted {
                    BadgeView.completed()
                }
            }
        }
    }
}

struct TrackSelectionListGridCellBadgesView_Previews: PreviewProvider {
    static var previews: some View {
        TrackSelectionListGridCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBeta: true,
            isCompleted: true
        )
        .padding()

        TrackSelectionListGridCellBadgesView(
            isSelected: true,
            isIdeRequired: true,
            isBeta: true,
            isCompleted: true
        )
        .padding()
        .preferredColorScheme(.dark)
    }
}
