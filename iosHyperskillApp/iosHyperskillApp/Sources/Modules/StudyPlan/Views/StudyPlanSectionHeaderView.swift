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
