import shared
import SwiftUI

extension StudyPlanSectionItemIconView {
    struct Appearance {
        let arrowWidthHeight: CGFloat = 32

        let defaultWidthHeight: CGFloat = 12
    }
}

struct StudyPlanSectionItemIconView: View {
    private(set) var appearance = Appearance()

    let itemState: StudyPlanWidgetViewStateSectionItemState

    private var stateImage: Image {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed:
            return Image(systemName: "checkmark")
        case StudyPlanWidgetViewStateSectionItemState.locked:
            return Image(systemName: "lock")
        case StudyPlanWidgetViewStateSectionItemState.next:
            return Image(Images.Home.ProblemOfDay.arrowUncompleted)
        case StudyPlanWidgetViewStateSectionItemState.skipped:
            return Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
        default:
            return Image(nil)
        }
    }

    private var renderingMode: Image.TemplateRenderingMode? {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed,
            StudyPlanWidgetViewStateSectionItemState.locked,
            StudyPlanWidgetViewStateSectionItemState.skipped:
            return .template
        case StudyPlanWidgetViewStateSectionItemState.next:
            return .original
        default:
            return nil
        }
    }

    private var contentMode: ContentMode {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed,
            StudyPlanWidgetViewStateSectionItemState.locked,
            StudyPlanWidgetViewStateSectionItemState.skipped:
                return .fit
        default:
            return .fill
        }
    }

    private var foregroundColor: Color? {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed:
            return Color(ColorPalette.secondary)
        case StudyPlanWidgetViewStateSectionItemState.locked,
            StudyPlanWidgetViewStateSectionItemState.skipped:
            return Color(ColorPalette.onSurfaceAlpha38)
        default:
            return nil
        }
    }

    private var widthHeight: CGFloat {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed,
            StudyPlanWidgetViewStateSectionItemState.locked,
            StudyPlanWidgetViewStateSectionItemState.skipped:
            return appearance.defaultWidthHeight
        case StudyPlanWidgetViewStateSectionItemState.next:
            return appearance.arrowWidthHeight
        default:
            return 0
        }
    }

    var body: some View {
        stateImage
            .resizable()
            .renderingMode(renderingMode)
            .aspectRatio(contentMode: contentMode)
            .foregroundColor(foregroundColor)
            .frame(widthHeight: widthHeight)
    }
}

struct StudyPlanSectionItemIconView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionItemIconView(itemState: StudyPlanWidgetViewStateSectionItemState.completed)
    }
}
