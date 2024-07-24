import SwiftUI

struct StudyPlanSectionItemBadgesView: View {
    let formattedProgress: String?

    let isIdeRequired: Bool

    private var isEmpty: Bool {
        formattedProgress?.isEmpty ?? true && !isIdeRequired
    }

    var body: some View {
        if !isEmpty {
            FlowLayoutCompatibility(
                configuration: .init(
                    spacing: LayoutInsets.smallInset,
                    fallbackLayout: .horizontal()
                )
            ) {
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

#if DEBUG
#Preview {
    StudyPlanSectionItemBadgesView(
        formattedProgress: "50%",
        isIdeRequired: true
    )
}
#endif
