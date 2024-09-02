import shared
import SwiftUI

extension StudyPlanSectionCompletedPageLoadingStateView {
    struct Appearance {
        let skeletonHeight: CGFloat

        let spacing = LayoutInsets.smallInset
        let iconWidthHeight: CGFloat = 24

        let buttonStyle = OutlineButtonStyle(
            borderColor: .border,
            alignment: .leading,
            paddingEdgeSet: [],
            backgroundColor: Color(ColorPalette.surface)
        )
    }
}

struct StudyPlanSectionCompletedPageLoadingStateView: View {
    let appearance: Appearance

    let completedPageLoadingState: StudyPlanWidgetViewStateSectionContentPageLoadingState

    let action: () -> Void

    var body: some View {
        switch completedPageLoadingState.wrapped ?? .hidden {
        case .hidden:
            EmptyView()
        case .loadMore:
            Button(
                action: action,
                label: {
                    HStack(alignment: .center, spacing: appearance.spacing) {
                        Image(.studyPlanSectionExpandCompleted)
                            .renderingMode(.template)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: appearance.iconWidthHeight)

                        Text(Strings.StudyPlan.expandCompletedButton)
                    }
                    .padding()
                }
            )
            .buttonStyle(appearance.buttonStyle)
        case .loading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHeight)
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        let appearance = StudyPlanSectionCompletedPageLoadingStateView.Appearance(skeletonHeight: 52)

        StudyPlanSectionCompletedPageLoadingStateView(
            appearance: appearance,
            completedPageLoadingState: .hidden,
            action: {}
        )
        StudyPlanSectionCompletedPageLoadingStateView(
            appearance: appearance,
            completedPageLoadingState: .loadMore,
            action: {}
        )
        StudyPlanSectionCompletedPageLoadingStateView(
            appearance: appearance,
            completedPageLoadingState: .loading,
            action: {}
        )
    }
    .padding()
}
#endif
