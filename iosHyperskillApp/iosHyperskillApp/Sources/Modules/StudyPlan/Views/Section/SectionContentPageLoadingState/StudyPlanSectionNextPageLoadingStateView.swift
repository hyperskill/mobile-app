import shared
import SwiftUI

extension StudyPlanSectionNextPageLoadingStateView {
    struct Appearance {
        let skeletonHeight: CGFloat
    }
}

struct StudyPlanSectionNextPageLoadingStateView: View {
    let appearance: Appearance

    let nextPageLoadingState: StudyPlanWidgetViewStateSectionContentPageLoadingState

    let action: () -> Void

    var body: some View {
        switch nextPageLoadingState.wrapped ?? .hidden {
        case .hidden:
            EmptyView()
        case .loadMore:
            Button(
                Strings.StudyPlan.loadMoreButton,
                action: action
            )
            .frame(maxWidth: .infinity, alignment: .center)
            .padding(.vertical)
        case .loading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHeight)
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        let appearance = StudyPlanSectionNextPageLoadingStateView.Appearance(skeletonHeight: 52)

        StudyPlanSectionNextPageLoadingStateView(appearance: appearance, nextPageLoadingState: .hidden, action: {})
        StudyPlanSectionNextPageLoadingStateView(appearance: appearance, nextPageLoadingState: .loadMore, action: {})
        StudyPlanSectionNextPageLoadingStateView(appearance: appearance, nextPageLoadingState: .loading, action: {})
    }
}
#endif
