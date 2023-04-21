import SwiftUI

extension StudyPlanSectionHeaderStatisticsView {
    struct Appearance {
        let statisticIconsSpacing: CGFloat = 4
        let statisticIconsWidthHeight: CGFloat = 14
    }
}

struct StudyPlanSectionHeaderStatisticsView: View {
    private(set) var appearance = Appearance()

    let formattedTopicsCount: String
    let formattedTimeToComplete: String

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            HStack(spacing: appearance.statisticIconsSpacing) {
                Image(Images.Track.About.topic)
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .foregroundColor(.primaryText)
                    .frame(widthHeight: appearance.statisticIconsWidthHeight)

                Text(formattedTopicsCount)
                    .font(.caption)
                    .foregroundColor(.primaryText)
            }

            HStack(spacing: appearance.statisticIconsSpacing) {
                Image(Images.Step.clock)
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .foregroundColor(.primaryText)
                    .frame(widthHeight: appearance.statisticIconsWidthHeight)

                Text(formattedTimeToComplete)
                    .font(.caption)
                    .foregroundColor(.primaryText)
            }
        }
    }
}

struct StudyPlanSectionHeaderStatisticsView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionHeaderStatisticsView(
            formattedTopicsCount: "5/12",
            formattedTimeToComplete: "13 hours"
        )
    }
}
