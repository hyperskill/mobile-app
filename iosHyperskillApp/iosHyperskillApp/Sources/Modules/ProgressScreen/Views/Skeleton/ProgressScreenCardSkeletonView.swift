import SwiftUI

struct ProgressScreenCardSkeletonView: View {
    var body: some View {
        SkeletonRoundedView()
            .frame(height: 107)
    }
}

struct ProgressScreenCardSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenCardSkeletonView()
    }
}
