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

                if isCompleted {
                    BadgeView.completed()
                }
            }
        }
    }
}

#if DEBUG
#Preview {
    TrackSelectionListGridCellBadgesView(
        isSelected: true,
        isIdeRequired: true,
        isBeta: true,
        isCompleted: true
    )
    .padding()
}

#Preview {
    TrackSelectionListGridCellBadgesView(
        isSelected: true,
        isIdeRequired: true,
        isBeta: true,
        isCompleted: true
    )
    .padding()
    .preferredColorScheme(.dark)
}
#endif
