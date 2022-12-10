import SwiftUI

struct TrackProgressSkeletonView: View {
    var body: some View {
        VStack {
            Group {
                SkeletonRoundedView()
                SkeletonRoundedView()
                SkeletonRoundedView()
            }
            .frame(height: 88)
        }
        .padding(.horizontal)
    }
}

struct TrackProgressSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TrackProgressSkeletonView()
    }
}
