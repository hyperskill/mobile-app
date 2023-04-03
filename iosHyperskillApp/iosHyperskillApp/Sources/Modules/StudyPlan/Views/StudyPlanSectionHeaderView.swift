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
    let onSectionTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            HStack {
                Text(section.title)
                    .font(.title3)
                    .bold()
                    .foregroundColor(
                        section.content is StudyPlanWidgetViewStateSectionContentCollapsed
                        ? .secondaryText
                        : .primaryText
                    )

                Spacer()

                Image(
                    systemName: section.content is StudyPlanWidgetViewStateSectionContentCollapsed
                    ? Images.SystemSymbol.Chevron.down
                    : Images.SystemSymbol.Chevron.up
                )
                .imageScale(.small)
                .foregroundColor(.secondaryText)
            }

            Text(section.subtitle)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            if let formattedTopicsCount = section.formattedTopicsCount,
               let formattedTimeToComplete = section.formattedTimeToComplete {
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
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(appearance.cornerRadius)
        .onTapGesture(perform: onSectionTap)
    }
}

struct StudyPlanSectionHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionHeaderView(
            section: StudyPlanWidgetViewStateSection.makePlaceholder(),
            onSectionTap: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
