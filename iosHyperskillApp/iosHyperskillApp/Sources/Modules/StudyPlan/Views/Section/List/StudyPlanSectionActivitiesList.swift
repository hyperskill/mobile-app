import shared
import SwiftUI

struct StudyPlanSectionActivitiesList: View {
    private static let animationDuration = 0.33

    let sectionItems: [StudyPlanWidgetViewStateSectionItem]
    let onTap: (Int64) -> Void

    @State private var opacity = 0.0

    var body: some View {
        ForEach(sectionItems, id: \.id) { item in
            StudyPlanSectionItemView(
                title: item.title,
                subtitle: item.subtitle,
                isClickable: item.isClickable,
                progress: item.progress,
                formattedProgress: item.formattedProgress,
                isIdeRequired: item.isIdeRequired,
                itemState: item.state.wrapped,
                onActivityTap: { onTap(item.id) }
            )
        }
        .opacity(opacity)
        .onAppear {
            withAnimation(.easeIn(duration: Self.animationDuration)) {
                opacity = 1
            }
        }
        .onDisappear {
            withAnimation(.easeOut(duration: Self.animationDuration)) {
                opacity = 0
            }
        }
    }
}

#if DEBUG
struct StudyPlanSectionActivitiesList_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            StudyPlanSectionActivitiesList(
                sectionItems: [
                    .makePlaceholder(state: .next),
                    .makePlaceholder(state: .idle)
                ],
                onTap: { _ in }
            )
        }
        .padding()
    }
}
#endif
