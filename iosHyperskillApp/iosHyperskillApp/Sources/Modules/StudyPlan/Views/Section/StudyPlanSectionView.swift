import shared
import SwiftUI

extension StudyPlanSectionView {
    struct Appearance {
        let skeletonHeight: CGFloat = 52
    }
}

struct StudyPlanSectionView: View {
    private(set) var appearance = Appearance()

    let section: StudyPlanWidgetViewStateSection

    let onSectionTap: (Int64) -> Void
    let onActivityTap: (Int64, Int64) -> Void
    let onRetryActivitiesLoadingTap: (Int64) -> Void
    let onLoadMoreActivitiesTap: (Int64) -> Void

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
                StudyPlanSectionErrorView {
                    onRetryActivitiesLoadingTap(section.id)
                }
            case .loading:
                SkeletonRoundedView()
                    .frame(height: appearance.skeletonHeight)
            case .content(let content):
                StudyPlanSectionActivitiesList(
                    sectionItems: content.sectionItems,
                    onTap: { activityID in
                        onActivityTap(activityID, section.id)
                    }
                )

                if content.isLoadAllTopicsButtonShown {
                    Button(
                        Strings.StudyPlan.loadMoreButton,
                        action: { onLoadMoreActivitiesTap(section.id) }
                    )
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.vertical)
                }
                if content.isNextPageLoadingShowed {
                    SkeletonRoundedView()
                        .frame(height: appearance.skeletonHeight)
                }
            }
        }
    }
}

#if DEBUG
#Preview {
    StudyPlanSectionView(
        section: StudyPlanWidgetViewStateSection.makePlaceholder(),
        onSectionTap: { _ in },
        onActivityTap: { _, _  in },
        onRetryActivitiesLoadingTap: { _ in },
        onLoadMoreActivitiesTap: { _ in }
    )
    .padding()
    .background(Color(ColorPalette.background))
}

#Preview {
    StudyPlanSectionView(
        section: StudyPlanWidgetViewStateSection.makePlaceholder(
            isNextPageLoadingShowed: true
        ),
        onSectionTap: { _ in },
        onActivityTap: { _, _  in },
        onRetryActivitiesLoadingTap: { _ in },
        onLoadMoreActivitiesTap: { _ in }
    )
    .padding()
    .background(Color(ColorPalette.background))
}

#Preview {
    StudyPlanSectionView(
        section: StudyPlanWidgetViewStateSection.makePlaceholder(
            isLoadAllTopicsButtonShown: true
        ),
        onSectionTap: { _ in },
        onActivityTap: { _, _  in },
        onRetryActivitiesLoadingTap: { _ in },
        onLoadMoreActivitiesTap: { _ in }
    )
    .padding()
    .background(Color(ColorPalette.background))
}

extension StudyPlanWidgetViewStateSection {
    static func makePlaceholder(
        isNextPageLoadingShowed: Bool = false,
        isLoadAllTopicsButtonShown: Bool = false
    ) -> StudyPlanWidgetViewStateSection {
        StudyPlanWidgetViewStateSection(
            id: 1,
            title: "Stage 1/6:  Hello, coffee!",
            subtitle: "Preconditions and postconditions",
            isCurrent: true,
            formattedTopicsCount: "3 / 5",
            formattedTimeToComplete: "~ 56 h",
            content: StudyPlanWidgetViewStateSectionContentContent(
                sectionItems: [
                    .makePlaceholder(state: .idle),
                    .makePlaceholder(state: .skipped),
                    .makePlaceholder(state: .completed),
                    .makePlaceholder(state: .next)
                ],
                isNextPageLoadingShowed: isNextPageLoadingShowed,
                isLoadAllTopicsButtonShown: isLoadAllTopicsButtonShown
            )
        )
    }
}
#endif
