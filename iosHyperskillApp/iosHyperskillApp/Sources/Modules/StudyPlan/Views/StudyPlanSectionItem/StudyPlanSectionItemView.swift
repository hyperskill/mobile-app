import shared
import SwiftUI

extension StudyPlanSectionItemView {
    struct Appearance {
        let backgroundProgressColor = Color(ColorPalette.overlayGreenAlpha7)

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
                        BadgeView(
                            text: formattedProgress,
                            style: .green
                        )
                    }

                    if item.isIdeRequired {
                        BadgeView.ideRequired()
                    }
                }

                Spacer()

                StudyPlanSectionItemIconView(itemState: item.state)
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
}

#if DEBUG
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
#endif

#if DEBUG
extension StudyPlanWidgetViewStateSectionItem {
    static func makePlaceholder(
        state: StudyPlanWidgetViewStateSectionItemState
    ) -> StudyPlanWidgetViewStateSectionItem {
        StudyPlanWidgetViewStateSectionItem(
            id: 123_412_341_234,
            title: "title",
            state: state,
            isIdeRequired: false,
            progress: 50,
            formattedProgress: "50%",
            hypercoinsAward: nil
        )
    }
}
#endif
