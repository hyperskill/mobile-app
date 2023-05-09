import SwiftUI

struct HomeSkeletonView: View {
    var body: some View {
        VStack(spacing: HomeView.Appearance().spacingBetweenContainers) {
            HomeSubheadlineView()
                .hidden()

            ProblemsLimitSkeletonView()
                .padding(.top, LayoutInsets.smallInset)

            ProblemOfDaySkeletonView()

            TopicsRepetitionsCardSkeletonView()

            TopicToDiscoverNextCardSkeletonView()
                .padding(.top, LayoutInsets.smallInset)

            Spacer()
        }
        .padding([.horizontal, .bottom])
    }
}

struct HomeSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        HomeSkeletonView()
    }
}
