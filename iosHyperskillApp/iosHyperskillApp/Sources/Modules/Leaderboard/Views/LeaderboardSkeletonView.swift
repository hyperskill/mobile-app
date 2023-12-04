import SwiftUI

extension LeaderboardSkeletonView {
    enum Appearance {
        static let spacing = LayoutInsets.defaultInset * 2

        static let firstSkeletonHeight: CGFloat = 132
        static let secondSkeletonHeight: CGFloat = Self.firstSkeletonHeight * 3
    }
}

struct LeaderboardSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: Appearance.spacing) {
                SkeletonRoundedView()
                    .frame(height: Appearance.firstSkeletonHeight)

                SkeletonRoundedView()
                    .frame(height: Appearance.secondSkeletonHeight)
            }
            .padding()
        }
    }
}

#Preview {
    LeaderboardSkeletonView()
}
