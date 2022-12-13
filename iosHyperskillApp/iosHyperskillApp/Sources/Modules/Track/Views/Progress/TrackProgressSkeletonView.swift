import SwiftUI

struct TrackProgressSkeletonView: View {
    var body: some View {
        VStack {
            ForEach(0..<3) { _ in
                SkeletonRoundedView()
                    .frame(height: 88)
            }
        }
        .padding(.horizontal)
    }
}

struct TrackProgressSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TrackProgressSkeletonView()
    }
}
