import SwiftUI

struct HomeSkeletonView: View {
    var body: some View {
        VStack(spacing: HomeView.Appearance().spacingBetweenContainers) {
            ProblemOfDaySkeletonView()
            TopicsRepetitionsCardSkeletonView()

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
