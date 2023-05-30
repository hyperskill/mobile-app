import SwiftUI

struct TrackSelectionDetailsDescriptionView: View {
    let description: String?

    let isBeta: Bool
    let isCompleted: Bool
    let isSelected: Bool

    let isBadgesVisible: Bool

    private var isEmpty: Bool {
        (description?.isEmpty ?? true) && !isBadgesVisible
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            CardView {
                VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                    if isBadgesVisible {
                        TrackSelectionListGridCellBadgesView(
                            isSelected: isSelected,
                            isIdeRequired: false,
                            isBeta: isBeta,
                            isCompleted: isCompleted
                        )
                    }

                    if let description, !description.isEmpty {
                        Text(description)
                            .font(.body)
                            .foregroundColor(.primaryText)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }
}

#if DEBUG
struct TrackSelectionDetailsDescriptionView_Previews: PreviewProvider {
    static var previews: some View {
        let description = """
Acquire key Python skills to establish a solid foundation for pursuing a career in Backend Development or Data Science.
"""
        Group {
            TrackSelectionDetailsDescriptionView(
                description: description,
                isBeta: true,
                isCompleted: true,
                isSelected: true,
                isBadgesVisible: true
            )

            TrackSelectionDetailsDescriptionView(
                description: description,
                isBeta: true,
                isCompleted: true,
                isSelected: true,
                isBadgesVisible: true
            )
            .preferredColorScheme(.dark)
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
