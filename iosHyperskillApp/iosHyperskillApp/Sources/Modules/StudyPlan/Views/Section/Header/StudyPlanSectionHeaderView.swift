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

    private var isCollapsed: Bool {
        section.content is StudyPlanWidgetViewStateSectionContentCollapsed
    }

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            HStack {
                Text(section.title)
                    .font(.title3)
                    .bold()
                    .foregroundColor(isCollapsed ? .secondaryText : .primaryText)

                Spacer()

                Image(systemName: Images.SystemSymbol.Chevron.down)
                    .imageScale(.small)
                    .rotation3DEffect(.degrees(isCollapsed ? 0 : 180), axis: (x: 1, y: 0, z: 0))
                    .foregroundColor(isCollapsed ? .secondaryText : .primaryText)
                    .scaleEffect(isCollapsed ? 1 : 1.5)
            }
            .animation(.easeInOut, value: isCollapsed)

            if let subtitle = section.subtitle {
                Text(subtitle)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
            }

            StudyPlanSectionHeaderStatisticsView(
                formattedTopicsCount: section.formattedTopicsCount,
                formattedTimeToComplete: section.formattedTimeToComplete
            )

            if section.isCurrentBadgeShown {
                BadgeView.current()
                    .animation(.easeInOut, value: section.isCurrentBadgeShown)
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(appearance.cornerRadius)
        .onTapGesture {
            withAnimation {
                onSectionTap(section.id)
            }
        }
        .animation(.easeOut, value: isCollapsed)
    }
}

#if DEBUG
struct StudyPlanSectionHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionHeaderView(
            section: .makePlaceholder(),
            onSectionTap: { _ in }
        )
        .previewLayout(.sizeThatFits)
    }
}
#endif
