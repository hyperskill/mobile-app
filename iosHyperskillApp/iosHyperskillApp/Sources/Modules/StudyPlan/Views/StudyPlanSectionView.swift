import shared
import SwiftUI

struct StudyPlanSectionView: View {
    let section: StudyPlanWidgetViewStateSection

    var body: some View {
        VStack(spacing: LayoutInsets.smallInset) {
            StudyPlanSectionHeaderView(section: section)

            switch StudyPlanWidgetViewStateSectionContentKs(section.content) {
            case .collapsed, .error:
                EmptyView()
            case .loading:
                SkeletonRoundedView()
                    .frame(height: 52)
            case .content(let content):
                ForEach(content.sectionItems, id: \.id) { item in
                    StudyPlanSectionItemView(item: item)
                }
            }
        }
    }
}

struct StudyPlanSectionView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionView(
            section: StudyPlanWidgetViewStateSection.makePlaceholder()
        )
        .padding()
        .background(Color(ColorPalette.background))
    }
}

#if DEBUG
extension StudyPlanWidgetViewStateSection {
    static func makePlaceholder() -> StudyPlanWidgetViewStateSection {
        StudyPlanWidgetViewStateSection(
            id: 1,
            title: "Stage 1/6:  Hello, coffee!",
            subtitle: "Preconditions and postconditions",
            formattedTopicsCount: "3 / 5",
            formattedTimeToComplete: "~ 56 h",
            content: StudyPlanWidgetViewStateSectionContentContent(
                sectionItems: [
                    StudyPlanWidgetViewStateSectionItem.makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.idle
                    ),
                    StudyPlanWidgetViewStateSectionItem.makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.skipped
                    ),
                    StudyPlanWidgetViewStateSectionItem.makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.completed
                    ),
                    StudyPlanWidgetViewStateSectionItem.makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.locked
                    ),
                    StudyPlanWidgetViewStateSectionItem.makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.next
                    )
                ]
            )
        )
    }
}
#endif
