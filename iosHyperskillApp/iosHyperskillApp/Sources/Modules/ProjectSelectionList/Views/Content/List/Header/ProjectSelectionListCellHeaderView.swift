import shared
import SwiftUI

extension ProjectSelectionListCellHeaderView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let ratingViewAppearance = RatingView.Appearance(
            imageSize: CGSize(width: 16, height: 16),
            imageColor: Color(ColorPalette.overlayYellow),
            textFont: .caption,
            textColor: .secondaryText
        )
    }
}

struct ProjectSelectionListCellHeaderView: View {
    private(set) var appearance = Appearance()

    let level: ProjectLevel?

    let isGraduate: Bool

    let averageRating: Double

    private var isEmpty: Bool {
        level == nil && !isGraduate && averageRating == 0
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: appearance.spacing) {
                if let level {
                    ProjectSelectionListCellProjectLevelView(level: level)
                }

                if isGraduate {
                    ProjectSelectionListCellProjectGraduateView()
                }

                Spacer()

                RatingView(
                    appearance: appearance.ratingViewAppearance,
                    rating: .number(value: averageRating, decimalPoints: 1)
                )
            }
        }
    }
}

struct ProjectSelectionListCellHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListCellHeaderView(level: .easy, isGraduate: true, averageRating: 4.7)
            ProjectSelectionListCellHeaderView(level: .medium, isGraduate: false, averageRating: 4.6)
            ProjectSelectionListCellHeaderView(level: .hard, isGraduate: false, averageRating: 0)
        }
        .padding()
    }
}
