import shared
import SwiftUI

struct StudyPlanSectionView: View {
    let section: StudyPlanWidgetViewStateSection

    var body: some View {
        VStack(spacing: LayoutInsets.smallInset) {
            HStack {
                Text(section.title)
                    .font(.title3)
                    .bold()
                    .foregroundColor(.primary)

                Spacer()

                
            }
        }
        .background(Color(ColorPalette.surface))
    }
}

struct StudyPlanSectionView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionView(
            section: StudyPlanWidgetViewStateSection(
                id: 1,
                title: "Stage 1/6:  Hello, coffee!",
                subtitle: "Preconditions and postconditions",
                formattedTopicsCount: "3 / 5",
                formattedTimeToComplete: "~ 56 h",
                content: StudyPlanWidgetViewStateSectionContentContent(
                    sectionItems: [
                        StudyPlanWidgetViewStateSectionItem(
                            id: 123412341234,
                            title: "test",
                            state: StudyPlanWidgetViewStateSectionItemState.next,
                            progress: 0.5,
                            formattedProgress: "50%"
                        ),
                        StudyPlanWidgetViewStateSectionItem(
                            id: 123412341234,
                            title: "test",
                            state: StudyPlanWidgetViewStateSectionItemState.locked,
                            progress: 0.5,
                            formattedProgress: "50%"
                        ),
                        StudyPlanWidgetViewStateSectionItem(
                            id: 123412341234,
                            title: "test",
                            state: StudyPlanWidgetViewStateSectionItemState.completed,
                            progress: 0.5,
                            formattedProgress: "50%"
                        ),
                        StudyPlanWidgetViewStateSectionItem(
                            id: 123412341234,
                            title: "test",
                            state: StudyPlanWidgetViewStateSectionItemState.skipped,
                            progress: 0.5,
                            formattedProgress: "50%"
                        ),
                        StudyPlanWidgetViewStateSectionItem(
                            id: 123412341234,
                            title: "test",
                            state: StudyPlanWidgetViewStateSectionItemState.idle,
                            progress: 0.5,
                            formattedProgress: "50%"
                        )
                    ]
                )
            )
        )
    }
}
