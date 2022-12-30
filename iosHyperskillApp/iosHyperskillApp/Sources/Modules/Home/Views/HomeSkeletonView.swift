import SwiftUI

extension HomeSkeletonView {
    struct Appearance {
        let toolbarSkeletonSize = CGSize(width: 56, height: 28)
    }
}

struct HomeSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: HomeView.Appearance().spacingBetweenContainers) {
            ProblemOfDaySkeletonView()
            TopicsRepetitionsCardSkeletonView()

            Spacer()
        }
        .toolbar {
            ToolbarItem(placement: .primaryAction) {
                HStack {
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
                    SkeletonRoundedView(appearance: .init(size: appearance.toolbarSkeletonSize))
                }
            }
        }
        .padding()
    }
}

struct HomeSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        HomeSkeletonView()
    }
}
