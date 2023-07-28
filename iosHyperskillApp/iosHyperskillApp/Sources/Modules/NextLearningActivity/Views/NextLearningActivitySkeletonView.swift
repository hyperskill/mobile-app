import SwiftUI

struct NextLearningActivitySkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 120)
    }
}

struct NextLearningActivitySkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsCardSkeletonView()
    }
}
