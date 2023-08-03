import SwiftUI

struct ProfileSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: ProfileView.Appearance().spacingBetweenContainers) {
                SkeletonRoundedView()
                    .frame(height: 96)

                SkeletonRoundedView()
                    .frame(height: 208)

                SkeletonRoundedView()
                    .frame(height: 71)

                SkeletonRoundedView()
                    .frame(height: 444)
            }
        }
        .frame(maxWidth: .infinity)
        .padding()
    }
}

struct ProfileSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSkeletonView()
    }
}
