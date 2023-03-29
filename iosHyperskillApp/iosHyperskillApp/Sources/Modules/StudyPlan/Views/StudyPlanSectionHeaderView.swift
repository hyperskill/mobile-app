import shared
import SwiftUI

extension StudyPlanSectionHeaderView {
    struct Appearance {
        let statisticIconsSpacing: CGFloat = 4
        let statisticIconsWidthHeight: CGFloat = 14
        let cornerRadius: CGFloat = 8
    }
}

struct StudyPlanSectionHeaderView: View {
    private(set) var appearance = Appearance()

    let section: StudyPlanWidgetViewStateSection

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            HStack {
                Text(section.title)
                    .font(.title3)
                    .bold()
                    .foregroundColor(.primary)

                Spacer()

                Image(
                    systemName: section.content is StudyPlanWidgetViewStateSectionContentCollapsed
                    ? Images.SystemSymbol.Chevron.up
                    : Images.SystemSymbol.Chevron.down
                )
                .imageScale(.small)
                .foregroundColor(.secondaryText)
            }

            Text(section.subtitle)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            HStack(spacing: LayoutInsets.defaultInset) {
                HStack(spacing: appearance.statisticIconsSpacing) {
                    Image(Images.Track.About.topic)
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .foregroundColor(.primaryText)
                        .frame(widthHeight: appearance.statisticIconsWidthHeight)

                    Text(section.formattedTopicsCount)
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

                    Text(section.formattedTimeToComplete)
                        .font(.caption)
                        .foregroundColor(.primaryText)
                }
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(appearance.cornerRadius)
    }
}

struct StudyPlanSectionHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionHeaderView(
            section: StudyPlanWidgetViewStateSection.makePlaceholder()
        )
        .previewLayout(.sizeThatFits)
    }
}
