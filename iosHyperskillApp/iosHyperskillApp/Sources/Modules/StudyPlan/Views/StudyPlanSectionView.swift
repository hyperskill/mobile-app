import shared
import SwiftUI

extension StudyPlanSectionView {
    struct Appearance {
        let skeletonHeight: CGFloat = 52
    }
}

struct StudyPlanSectionView: View {
    private(set) var apperance = Appearance()

    let section: StudyPlanWidgetViewStateSection

    let onSectionTap: (Int64) -> Void
    let onActivityTap: (Int64) -> Void
    let onRetryActivitiesLoadingTap: (Int64) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            StudyPlanSectionHeaderView(
                section: section,
                onSectionTap: onSectionTap
            )

            switch StudyPlanWidgetViewStateSectionContentKs(section.content) {
            case .collapsed:
                EmptyView()
            case .error:
                StudyPlanSectionContentErrorView {
                    onRetryActivitiesLoadingTap(section.id)
                }
            case .loading:
                SkeletonRoundedView()
                    .frame(height: apperance.skeletonHeight)
            case .content(let content):
                StudyPlanSectionActivitiesList(
                    sectionItems: content.sectionItems,
                    onTap: onActivityTap
                )
            }
        }
    }
}

#if DEBUG
struct StudyPlanSectionView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionView(
            section: StudyPlanWidgetViewStateSection.makePlaceholder(),
            onSectionTap: { _ in },
            onActivityTap: { _ in },
            onRetryActivitiesLoadingTap: { _ in }
        )
        .padding()
        .background(Color(ColorPalette.background))
    }
}

extension StudyPlanWidgetViewStateSection {
    static func makePlaceholder() -> StudyPlanWidgetViewStateSection {
        StudyPlanWidgetViewStateSection(
            id: 1,
            title: "Stage 1/6:  Hello, coffee!",
            subtitle: "Preconditions and postconditions",
            isCurrent: true,
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
