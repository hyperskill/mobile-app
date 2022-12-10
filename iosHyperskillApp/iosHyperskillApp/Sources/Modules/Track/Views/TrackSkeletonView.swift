import SwiftUI

struct TrackSkeletonView: View {
    private static let trackViewAppearance = TrackView.Appearance()

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: Self.trackViewAppearance.spacingBetweenContainers) {
                TrackHeaderSkeletonView()
                TrackTopicsToDiscoverNextSkeletonView()
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
