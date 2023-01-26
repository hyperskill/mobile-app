import SwiftUI

struct TrackSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: TrackView.Appearance().spacingBetweenContainers) {
                TrackHeaderSkeletonView()
                TrackTopicsToDiscoverNextSkeletonBlockView()
                TrackProgressSkeletonView()
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct TrackSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TrackSkeletonView()
    }
}
