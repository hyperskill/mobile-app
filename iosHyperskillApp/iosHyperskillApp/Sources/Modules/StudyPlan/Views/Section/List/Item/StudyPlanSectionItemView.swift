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

    let title: String
    let subtitle: String?

    let isClickable: Bool

    let progress: Int32
    let formattedProgress: String?

    let isIdeRequired: Bool

    let itemState: StudyPlanWidgetViewStateSectionItemStateWrapper?

    let onActivityTap: () -> Void

    var body: some View {
        Button(
            action: onActivityTap,
            label: buildContent
        )
        .disabled(!isClickable)
        .buttonStyle(appearance.buttonStyle)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildContent() -> some View {
        HStack(alignment: .top, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                buildTextContent()

                StudyPlanSectionItemBadgesView(
                    formattedProgress: formattedProgress,
                    isIdeRequired: isIdeRequired
                )
            }

            if let itemState {
                StudyPlanSectionItemIconView(itemState: itemState)
            }
        }
        .padding()
        .backgroundProgress(progress: progress)
    }

    @ViewBuilder
    private func buildTextContent() -> some View {
        VStack(alignment: .leading, spacing: appearance.textGroupSpacing) {
            Text(title)
                .font(.body)

            if let subtitle {
                Text(subtitle)
                    .font(.headline)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .foregroundColor(isClickable ? .primaryText : .secondaryText)
    }
}

#if DEBUG
struct StudyPlanSectionItemView_Previews: PreviewProvider {
    static var previews: some View {
        let idlePlaceholder = StudyPlanWidgetViewStateSectionItem
            .makePlaceholder(state: .idle)

        let completedPlaceholder = StudyPlanWidgetViewStateSectionItem
            .makePlaceholder(
                state: .completed,
                isIdeRequired: true,
                progress: 50,
                formattedProgress: "50%"
            )

        let nextPlaceholder = StudyPlanWidgetViewStateSectionItem
            .makePlaceholder(state: .next, subtitle: "Hello, coffee!")

        let skippedPlaceholder = StudyPlanWidgetViewStateSectionItem
            .makePlaceholder(state: .skipped)

        VStack(spacing: LayoutInsets.defaultInset) {
            StudyPlanSectionItemView(
                title: idlePlaceholder.title,
                subtitle: idlePlaceholder.subtitle,
                isClickable: idlePlaceholder.isClickable,
                progress: idlePlaceholder.progress,
                formattedProgress: idlePlaceholder.formattedProgress,
                isIdeRequired: idlePlaceholder.isIdeRequired,
                itemState: idlePlaceholder.state.wrapped,
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                title: completedPlaceholder.title,
                subtitle: completedPlaceholder.subtitle,
                isClickable: completedPlaceholder.isClickable,
                progress: completedPlaceholder.progress,
                formattedProgress: completedPlaceholder.formattedProgress,
                isIdeRequired: completedPlaceholder.isIdeRequired,
                itemState: completedPlaceholder.state.wrapped,
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                title: nextPlaceholder.title,
                subtitle: nextPlaceholder.subtitle,
                isClickable: nextPlaceholder.isClickable,
                progress: nextPlaceholder.progress,
                formattedProgress: nextPlaceholder.formattedProgress,
                isIdeRequired: nextPlaceholder.isIdeRequired,
                itemState: nextPlaceholder.state.wrapped,
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                title: skippedPlaceholder.title,
                subtitle: skippedPlaceholder.subtitle,
                isClickable: skippedPlaceholder.isClickable,
                progress: skippedPlaceholder.progress,
                formattedProgress: skippedPlaceholder.formattedProgress,
                isIdeRequired: skippedPlaceholder.isIdeRequired,
                itemState: skippedPlaceholder.state.wrapped,
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
