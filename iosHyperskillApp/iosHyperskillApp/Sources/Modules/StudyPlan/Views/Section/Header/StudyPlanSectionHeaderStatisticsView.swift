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
                    buildItemView(imageName: Images.Track.About.topic, title: formattedTopicsCount)
                }
                if let formattedTimeToComplete {
                    buildItemView(imageName: Images.Step.clock, title: formattedTimeToComplete)
                }
            }
        }
    }

    @ViewBuilder
    private func buildItemView(imageName: String, title: String) -> some View {
        HStack(spacing: appearance.statisticIconsSpacing) {
            Image(imageName)
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

struct StudyPlanSectionHeaderStatisticsView_Previews: PreviewProvider {
    static var previews: some View {
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
