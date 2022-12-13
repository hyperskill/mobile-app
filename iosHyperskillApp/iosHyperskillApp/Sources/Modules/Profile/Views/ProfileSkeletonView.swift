import SwiftUI

struct ProfileSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: ProfileView.Appearance().spacingBetweenContainers) {
                SkeletonRoundedView()
                    .frame(height: 96)

                SkeletonRoundedView()
                    .frame(height: 156)

                HStack {
                    SkeletonRoundedView()
                    SkeletonRoundedView()
                    SkeletonRoundedView()
                }
                .frame(height: 74)

                SkeletonRoundedView()
                    .frame(height: 100)

                SkeletonRoundedView()
                    .frame(height: 379)
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
