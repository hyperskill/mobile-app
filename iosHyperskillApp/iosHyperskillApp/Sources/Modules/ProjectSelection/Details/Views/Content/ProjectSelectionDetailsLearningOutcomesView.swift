import SwiftUI

extension ProjectSelectionDetailsLearningOutcomesView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let stepTextViewAppearance = StepTextUIKitView.Appearance(
            textFont: .preferredFont(forTextStyle: .body),
            textColor: .primaryText
        )
    }
}

struct ProjectSelectionDetailsLearningOutcomesView: View {
    private(set) var appearance = Appearance()

    let description: String?

    let isSelected: Bool
    let isIdeRequired: Bool
    let isBeta: Bool
    let isBestRated: Bool
    let isFastestToComplete: Bool

    let isBadgesVisible: Bool

    private var isEmpty: Bool {
        (description?.isEmpty ?? true) && !isBadgesVisible
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            CardView {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    if isBadgesVisible {
                        ProjectSelectionListGridCellBadgesView(
                            isSelected: isSelected,
                            isIdeRequired: isIdeRequired,
                            isBeta: isBeta,
                            isBestRated: isBestRated,
                            isFastestToComplete: isFastestToComplete
                        )
                    }

                    if let description, !description.isEmpty {
                        VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                            Text(Strings.ProjectSelectionDetails.learningOutcomesTitle)
                                .font(.headline)
                                .foregroundColor(.primaryText)

                            StepTextView(
                                text: description,
                                appearance: appearance.stepTextViewAppearance
                            )
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }
}

#if DEBUG
struct ProjectSelectionDetailsLearningOutcomesView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionDetailsLearningOutcomesView(
            description: """
<p>This project is aimed at our beginners. \
It helps you understand some syntax basics and learn how to work with variables, data storage types such as lists, \
and while loops.</p>
""",
            isSelected: true,
            isIdeRequired: true,
            isBeta: true,
            isBestRated: true,
            isFastestToComplete: true,
            isBadgesVisible: true
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
