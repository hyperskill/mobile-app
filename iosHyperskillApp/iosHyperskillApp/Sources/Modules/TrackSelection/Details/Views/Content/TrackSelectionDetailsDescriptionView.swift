import SwiftUI

struct TrackSelectionDetailsDescriptionView: View {
    let description: String?

    let isBeta: Bool
    let isCompleted: Bool
    let isSelected: Bool

    private var isEmpty: Bool {
        (description?.isEmpty ?? true) && isBadgesEmpty
    }

    private var isBadgesEmpty: Bool {
        !isBeta && !isCompleted && !isSelected
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            TrackSelectionDetailsBlockView {
                VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                    if !isBadgesEmpty {
                        HStack(spacing: LayoutInsets.smallInset) {
                            if isCompleted {
                                BadgeView.completed()
                            }
                            if isSelected {
                                BadgeView.selected()
                            }
                            if isBeta {
                                BadgeView.beta()
                            }
                        }
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
                isSelected: true
            )

            TrackSelectionDetailsDescriptionView(
                description: description,
                isBeta: true,
                isCompleted: true,
                isSelected: true
            )
            .preferredColorScheme(.dark)
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
