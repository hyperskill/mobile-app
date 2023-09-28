import SwiftUI

extension StudyPlanSectionHeaderStatisticsView {
    struct Appearance {
        let statisticIconsSpacing: CGFloat = 4
        let statisticIconsWidthHeight: CGFloat = 14
    }
}

struct StudyPlanSectionHeaderStatisticsView: View {
    private(set) var appearance = Appearance()

    let formattedTopicsCount: String?
    let formattedTimeToComplete: String?

    private var isEmpty: Bool {
        (formattedTopicsCount?.isEmpty ?? true) && (formattedTimeToComplete?.isEmpty ?? true)
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            HStack(spacing: LayoutInsets.defaultInset) {
                if let formattedTopicsCount {
                    buildItemView(imageResource: .topic, title: formattedTopicsCount)
                }
                if let formattedTimeToComplete {
                    buildItemView(imageResource: .stepTimeToComplete, title: formattedTimeToComplete)
                }
            }
        }
    }

    @ViewBuilder
    private func buildItemView(imageResource: ImageResource, title: String) -> some View {
        HStack(spacing: appearance.statisticIconsSpacing) {
            Image(imageResource)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.statisticIconsWidthHeight)

            Text(title)
                .font(.caption)
        }
        .foregroundColor(.primaryText)
    }
}

#Preview {
    Group {
        StudyPlanSectionHeaderStatisticsView(
            formattedTopicsCount: "5/12",
            formattedTimeToComplete: "13 hours"
        )

        StudyPlanSectionHeaderStatisticsView(
            formattedTopicsCount: nil,
            formattedTimeToComplete: "13 hours"
        )
    }
}
