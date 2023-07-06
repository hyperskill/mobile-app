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

    let itemState: StudyPlanWidgetViewStateSectionItemStateWrapper

    private var stateImage: Image {
        switch itemState {
        case .completed:
            return Image(systemName: "checkmark")
        case .locked:
            return Image(systemName: "lock")
        case .next:
            return Image(Images.Home.ProblemOfDay.arrowUncompleted)
        case .skipped:
            return Image(Images.Common.skip)
        case .idle:
            return Image(nil)
        }
    }

    private var renderingMode: Image.TemplateRenderingMode? {
        switch itemState {
        case .completed, .locked, .skipped:
            return .template
        case .next:
            return .original
        case .idle:
            return nil
        }
    }

    private var contentMode: ContentMode {
        switch itemState {
        case .completed, .locked, .skipped:
            return .fit
        case .idle, .next:
            return .fill
        }
    }

    private var foregroundColor: Color? {
        switch itemState {
        case .completed:
            return Color(ColorPalette.secondary)
        case .locked, .skipped:
            return Color(ColorPalette.onSurfaceAlpha38)
        case .idle, .next:
            return nil
        }
    }

    private var widthHeight: CGFloat {
        switch itemState {
        case .completed, .locked, .skipped:
            return appearance.defaultWidthHeight
        case .next:
            return appearance.arrowWidthHeight
        case .idle:
            return 0
        }
    }

    var body: some View {
        if itemState == .idle {
            EmptyView()
        } else {
            stateImage
                .resizable()
                .renderingMode(renderingMode)
                .aspectRatio(contentMode: contentMode)
                .foregroundColor(foregroundColor)
                .frame(widthHeight: widthHeight)
        }
    }
}

struct StudyPlanSectionItemIconView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            StudyPlanSectionItemIconView(itemState: .idle)
            StudyPlanSectionItemIconView(itemState: .next)
            StudyPlanSectionItemIconView(itemState: .locked)
            StudyPlanSectionItemIconView(itemState: .skipped)
            StudyPlanSectionItemIconView(itemState: .completed)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
