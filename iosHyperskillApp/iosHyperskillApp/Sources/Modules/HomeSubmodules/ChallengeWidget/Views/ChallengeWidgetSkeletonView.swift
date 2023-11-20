import SwiftUI

struct ChallengeWidgetSkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 128)
    }
}

#Preview {
    ChallengeWidgetSkeletonView()
        .padding()
}
