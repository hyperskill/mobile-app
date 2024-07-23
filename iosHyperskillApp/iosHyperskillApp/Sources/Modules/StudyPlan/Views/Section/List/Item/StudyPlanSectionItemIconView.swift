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
            Image(systemName: "checkmark")
        case .locked:
            Image(systemName: "lock")
        case .next:
            Image(Images.Home.ProblemOfDay.arrowUncompleted)
        case .skipped:
            Image(Images.Common.skip)
        case .idle:
            Image(nil)
        }
    }

    private var renderingMode: Image.TemplateRenderingMode? {
        switch itemState {
        case .completed, .locked, .skipped:
            .template
        case .next:
            .original
        case .idle:
            nil
        }
    }

    private var contentMode: ContentMode {
        switch itemState {
        case .completed, .locked, .skipped:
            .fit
        case .idle, .next:
            .fill
        }
    }

    private var foregroundColor: Color? {
        switch itemState {
        case .completed:
            Color(ColorPalette.secondary)
        case .locked, .skipped:
            Color(ColorPalette.onSurfaceAlpha38)
        case .idle, .next:
            nil
        }
    }

    private var widthHeight: CGFloat {
        switch itemState {
        case .completed, .locked, .skipped:
            appearance.defaultWidthHeight
        case .next:
            appearance.arrowWidthHeight
        case .idle:
            0
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

#if DEBUG
#Preview {
    VStack(spacing: LayoutInsets.defaultInset) {
        StudyPlanSectionItemIconView(itemState: .idle)
        StudyPlanSectionItemIconView(itemState: .next)
        StudyPlanSectionItemIconView(itemState: .locked)
        StudyPlanSectionItemIconView(itemState: .skipped)
        StudyPlanSectionItemIconView(itemState: .completed)
    }
    .padding()
}
#endif
