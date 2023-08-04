import SwiftUI

struct ProfileSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: ProfileView.Appearance().spacingBetweenContainers) {
                SkeletonRoundedView()
                    .frame(height: 96)

                SkeletonRoundedView()
                    .frame(height: 204)

                SkeletonRoundedView()
                    .frame(height: 63)

                SkeletonRoundedView()
                    .frame(height: 235)

                HStack {
                    SkeletonRoundedView()
                    SkeletonRoundedView()
                    SkeletonRoundedView()
                }
                .frame(height: 82)

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
