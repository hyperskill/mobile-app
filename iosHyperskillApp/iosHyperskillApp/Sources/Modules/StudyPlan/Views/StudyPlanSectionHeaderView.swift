import shared
import SwiftUI

extension StudyPlanSectionHeaderView {
    struct Appearance {
        let cornerRadius: CGFloat = 8
    }
}

struct StudyPlanSectionHeaderView: View {
    private(set) var appearance = Appearance()

    let section: StudyPlanWidgetViewStateSection
    let onSectionTap: (Int64) -> Void

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
                StudyPlanSectionHeaderStatisticsView(
                    formattedTopicsCount: formattedTopicsCount,
                    formattedTimeToComplete: formattedTimeToComplete
                )
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(appearance.cornerRadius)
        .onTapGesture { onSectionTap(section.id) }
    }
}

#if DEBUG
struct StudyPlanSectionHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionHeaderView(
            section: StudyPlanWidgetViewStateSection.makePlaceholder(),
            onSectionTap: { _ in }
        )
        .previewLayout(.sizeThatFits)
    }
}
#endif
