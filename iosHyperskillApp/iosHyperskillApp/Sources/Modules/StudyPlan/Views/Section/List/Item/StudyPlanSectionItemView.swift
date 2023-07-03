import shared
import SwiftUI

extension StudyPlanSectionItemView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let textGroupSpacing = LayoutInsets.smallInset

        let progressBarBackgroundColor = Color(ColorPalette.overlayGreenAlpha7)

        let buttonStyle = OutlineButtonStyle(
            borderColor: .border,
            alignment: .leading,
            paddingEdgeSet: [],
            backgroundColor: Color(ColorPalette.surface)
        )
    }
}

struct StudyPlanSectionItemView: View {
    private(set) var appearance = Appearance()

    let item: StudyPlanWidgetViewStateSectionItem
    let onActivityTap: () -> Void

    var body: some View {
        Button(
            action: onActivityTap,
            label: buildContent
        )
        .disabled(!item.isClickable)
        .buttonStyle(appearance.buttonStyle)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildContent() -> some View {
        HStack(alignment: .top, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                buildTextContent()

                StudyPlanSectionItemBadgesView(
                    formattedProgress: item.formattedProgress,
                    isIdeRequired: item.isIdeRequired
                )
            }

            if let itemState = item.state.wrapped {
                StudyPlanSectionItemIconView(itemState: itemState)
            }
        }
        .padding()
        .background(buildProgressBar(Float(item.progress)))
    }

    @ViewBuilder
    private func buildTextContent() -> some View {
        VStack(alignment: .leading, spacing: appearance.textGroupSpacing) {
            Text(item.title)
                .font(.body)

            if let subtitle = item.subtitle {
                Text(subtitle)
                    .font(.headline)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .foregroundColor(item.isClickable ? .primaryText : .secondaryText)
    }

    @ViewBuilder
    private func buildProgressBar(_ progress: Float) -> some View {
        #warning("Migrate to the BackgroundProgressView")
        GeometryReader { geometry in
            Rectangle()
                .stroke(lineWidth: 0)
                .background(appearance.progressBarBackgroundColor)
                .cornerRadius(appearance.buttonStyle.cornerRadius)
                .frame(width: geometry.size.width * CGFloat(progress) / 100)
        }
    }
}

#if DEBUG
struct StudyPlanSectionItemView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            StudyPlanSectionItemView(
                item: .makePlaceholder(state: .idle),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: .makePlaceholder(
                    state: .completed,
                    isIdeRequired: true,
                    progress: 50,
                    formattedProgress: "50%"
                ),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: .makePlaceholder(state: .locked),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: .makePlaceholder(state: .next, subtitle: "Hello, coffee!"),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: .makePlaceholder(state: .skipped),
                onActivityTap: {}
            )
        }
        .padding()
    }
}

extension StudyPlanWidgetViewStateSectionItem {
    static func makePlaceholder(
        state: StudyPlanWidgetViewStateSectionItemState,
        title: String = "Work on project. Stage: 1/6",
        subtitle: String? = nil,
        isIdeRequired: Bool = false,
        progress: Int32 = 0,
        formattedProgress: String? = nil
    ) -> StudyPlanWidgetViewStateSectionItem {
        StudyPlanWidgetViewStateSectionItem(
            id: 123_412_341_234,
            title: title,
            subtitle: subtitle,
            state: state,
            isIdeRequired: isIdeRequired,
            progress: progress,
            formattedProgress: formattedProgress,
            hypercoinsAward: nil
        )
    }
}
#endif
