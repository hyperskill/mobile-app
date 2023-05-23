import SwiftUI

extension ProjectSelectionListGridCellHeaderView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let ratingViewAppearance = StarRatingView.Appearance(
            imageSize: CGSize(width: 16, height: 16),
            imageColor: Color(ColorPalette.overlayYellow),
            textFont: .caption,
            textColor: .secondaryText
        )
    }
}

struct ProjectSelectionListGridCellHeaderView: View {
    private(set) var appearance = Appearance()

    let level: SharedProjectLevelWrapper?

    let isGraduate: Bool

    let averageRating: String

    private var isEmpty: Bool {
        level == nil && !isGraduate && averageRating.isEmpty
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: appearance.spacing) {
                if let level {
                    ProjectSelectionListGridCellProjectLevelView(level: level)
                }

                if isGraduate {
                    ProjectSelectionListGridCellProjectGraduateView()
                }

                Spacer()

                StarRatingView(
                    appearance: appearance.ratingViewAppearance,
                    rating: .string(averageRating)
                )
            }
        }
    }
}

struct ProjectSelectionListGridCellHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListGridCellHeaderView(level: .easy, isGraduate: true, averageRating: "4.7")
            ProjectSelectionListGridCellHeaderView(level: .medium, isGraduate: false, averageRating: "4.6")
            ProjectSelectionListGridCellHeaderView(level: .hard, isGraduate: false, averageRating: "")
        }
        .padding()
    }
}
