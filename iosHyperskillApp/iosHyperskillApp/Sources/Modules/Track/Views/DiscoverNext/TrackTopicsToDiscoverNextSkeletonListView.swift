import SwiftUI

struct TrackTopicsToDiscoverNextSkeletonListView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ForEach(0..<3) { _ in
                SkeletonRoundedView()
                    .frame(height: 44)
            }
        }
    }
}

struct TrackTopicsToDiscoverNextSkeletonItemView_Previews: PreviewProvider {
    static var previews: some View {
        TrackTopicsToDiscoverNextSkeletonListView()
    }
}
