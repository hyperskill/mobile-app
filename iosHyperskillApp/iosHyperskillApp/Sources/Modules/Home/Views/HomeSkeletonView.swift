import SwiftUI

struct HomeSkeletonView: View {
    var body: some View {
        VStack(spacing: HomeView.Appearance().spacingBetweenContainers) {
            HomeSubheadlineView()
                .hidden()

            ProblemOfDaySkeletonView()

            TopicsRepetitionsCardSkeletonView()

            Spacer()
        }
        .padding([.horizontal, .bottom])
    }
}

#Preview {
    HomeSkeletonView()
}
