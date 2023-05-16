import SwiftUI

struct StudyPlanSectionItemBadgesView: View {
    let formattedProgress: String?

    let isIdeRequired: Bool

    private var isEmpty: Bool {
        formattedProgress?.isEmpty ?? true && !isIdeRequired
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: LayoutInsets.smallInset) {
                if let formattedProgress {
                    BadgeView(text: formattedProgress, style: .green)
                }

                if isIdeRequired {
                    BadgeView.ideRequired()
                }
            }
        }
    }
}

struct StudyPlanSectionItemBadgesView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionItemBadgesView(
            formattedProgress: "50%",
            isIdeRequired: true
        )
    }
}
