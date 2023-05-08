import SwiftUI

struct TopicToDiscoverNextCardSkeletonView: View {
    private static let skeletonHeight: CGFloat = 64

    var body: some View {
        SkeletonRoundedView()
            .frame(height: Self.skeletonHeight)
    }
}

struct TopicToDiscoverNextCardSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TopicToDiscoverNextCardSkeletonView()
            .padding()
    }
}
