import shared
import SwiftUI

struct StudyPlanSectionItemView: View {
    let item: StudyPlanWidgetViewStateSectionItem

    var body: some View {
        HStack {
            Text(String(item.id))
                .font(.body)
                .foregroundColor(.primaryText)

            Spacer()
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder()
    }
}

struct StudyPlanSectionItemView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionItemView(
            item: StudyPlanWidgetViewStateSectionItem
                .makePlaceholder(
                    state: StudyPlanWidgetViewStateSectionItemState.idle
                )
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}

#if DEBUG
extension StudyPlanWidgetViewStateSectionItem {
    static func makePlaceholder(state: StudyPlanWidgetViewStateSectionItemState) -> StudyPlanWidgetViewStateSectionItem {
        StudyPlanWidgetViewStateSectionItem(
            id: 123_412_341_234,
            title: "test",
            state: StudyPlanWidgetViewStateSectionItemState.idle,
            progress: 0.5,
            formattedProgress: "50%"
        )
    }
}
#endif
