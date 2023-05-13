import SwiftUI

extension BadgeView {
    // MARK: Common

    static func ideRequired() -> BadgeView {
        BadgeView(
            text: Strings.StageImplement.UnsupportedModal.title,
            style: .violet
        )
    }

    // MARK: StudyPlan

    static func studyPlanCurrent() -> BadgeView {
        BadgeView(text: Strings.StudyPlan.badgeCurrent, style: .blue)
    }

    // MARK: Freemium

    static func freemiumSolveUnlimited() -> BadgeView {
        BadgeView(text: Strings.Home.solveUnlimited, style: .violet)
    }

    static func freemiumRepeatUnlimited() -> BadgeView {
        BadgeView(text: Strings.Home.repeatUnlimited, style: .violet)
    }

    // MARK: ProjectSelectionList

    static func projectSelectionListSelected() -> BadgeView {
        BadgeView(text: Strings.ProjectSelectionList.List.Badge.selected, style: .blue)
    }

    static func projectSelectionListBestRating() -> BadgeView {
        BadgeView(text: Strings.ProjectSelectionList.List.Badge.bestRating, style: .blue)
    }

    static func projectSelectionListFastestToComplete() -> BadgeView {
        BadgeView(text: Strings.ProjectSelectionList.List.Badge.fastestToComplete, style: .blue)
    }
}

// MARK: - BadgeViewConcreateTypes_Previews: PreviewProvider -

struct BadgeViewConcreateTypes_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            BadgeView.ideRequired()

            BadgeView.studyPlanCurrent()

            BadgeView.freemiumSolveUnlimited()
            BadgeView.freemiumRepeatUnlimited()

            BadgeView.projectSelectionListSelected()
            BadgeView.projectSelectionListBestRating()
            BadgeView.projectSelectionListFastestToComplete()
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
