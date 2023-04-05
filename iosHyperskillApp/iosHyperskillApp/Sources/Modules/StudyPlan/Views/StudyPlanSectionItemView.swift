import shared
import SwiftUI

extension StudyPlanSectionItemView {
    struct Appearance {
        let arrowIconSize: CGFloat = 32

        let rightDetailIconWidthHeight: CGFloat = 12

        let backgroundProgressColor = Color(ColorPalette.overlayGreenAlpha7)

        let progressBadgeForegroundColor = Color(ColorPalette.secondary)
        let progressBadgeBackgroundColor = Color(ColorPalette.overlayGreenAlpha12)
        let progressBadgeCornerRadius: CGFloat = 4
        let progressBadgeInsets = LayoutInsets(horizontal: 8, vertical: 4)

        var buttonStyle = OutlineButtonStyle(
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
        Button(action: onActivityTap) {
            HStack(alignment: .top) {
                VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                    Text(item.title)
                        .font(.body)
                        .foregroundColor(item.isClickable ? .primaryText : .secondaryText)
                        .padding(.trailing)

                    if let formattedProgress = item.formattedProgress {
                        Text(formattedProgress)
                            .font(.caption)
                            .foregroundColor(appearance.progressBadgeForegroundColor)
                            .padding(appearance.progressBadgeInsets.edgeInsets)
                            .background(appearance.progressBadgeBackgroundColor)
                            .cornerRadius(appearance.progressBadgeCornerRadius)
                    }
                }

                Spacer()

                buildItemIcon(itemState: item.state)
            }
            .padding()
            .frame(minHeight: appearance.buttonStyle.minHeight)
            .background(
                buildCompletenessBar(
                    completenessPercentage: Float(truncating: item.progress ?? 0)
                )
            )
        }
        .disabled(!item.isClickable)
        .buttonStyle(appearance.buttonStyle)
        .buttonStyle(BounceButtonStyle())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildCompletenessBar(completenessPercentage: Float) -> some View {
        GeometryReader { geometry in
            Rectangle()
                .stroke(lineWidth: 0)
                .background(appearance.backgroundProgressColor)
                .cornerRadius(appearance.buttonStyle.cornerRadius)
                .frame(
                    width: geometry.size.width * CGFloat(completenessPercentage) / 100
                )
        }
    }

    @ViewBuilder
    private func buildItemIcon(
        itemState: StudyPlanWidgetViewStateSectionItemState
    ) -> some View {
        switch itemState {
        case StudyPlanWidgetViewStateSectionItemState.completed:
            Image(systemName: "checkmark")
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .foregroundColor(Color(ColorPalette.secondary))
                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
        case StudyPlanWidgetViewStateSectionItemState.locked:
            Image(systemName: "lock")
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
        case StudyPlanWidgetViewStateSectionItemState.next:
            Image(Images.Home.ProblemOfDay.arrowUncompleted)
                .renderingMode(.original)
                .resizable()
                .frame(widthHeight: appearance.arrowIconSize)
        case StudyPlanWidgetViewStateSectionItemState.skipped:
            Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
        default:
            EmptyView()
        }
    }
}

struct StudyPlanSectionItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StudyPlanSectionItemView(
                item: StudyPlanWidgetViewStateSectionItem
                    .makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.idle
                    ),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: StudyPlanWidgetViewStateSectionItem
                    .makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.completed
                    ),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: StudyPlanWidgetViewStateSectionItem
                    .makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.locked
                    ),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: StudyPlanWidgetViewStateSectionItem
                    .makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.next
                    ),
                onActivityTap: {}
            )

            StudyPlanSectionItemView(
                item: StudyPlanWidgetViewStateSectionItem
                    .makePlaceholder(
                        state: StudyPlanWidgetViewStateSectionItemState.skipped
                    ),
                onActivityTap: {}
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}

#if DEBUG
extension StudyPlanWidgetViewStateSectionItem {
    static func makePlaceholder(
        state: StudyPlanWidgetViewStateSectionItemState
    ) -> StudyPlanWidgetViewStateSectionItem {
        StudyPlanWidgetViewStateSectionItem(
            id: 123_412_341_234,
            title: "title",
            state: state,
            progress: 50,
            formattedProgress: "50%"
        )
    }
}
#endif
