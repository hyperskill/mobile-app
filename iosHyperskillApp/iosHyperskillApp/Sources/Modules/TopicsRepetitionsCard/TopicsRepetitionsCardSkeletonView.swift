import SwiftUI

struct TopicsRepetitionsCardSkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 116)
    }
}

struct TopicsRepetitionsCardSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsCardSkeletonView()
    }
}
