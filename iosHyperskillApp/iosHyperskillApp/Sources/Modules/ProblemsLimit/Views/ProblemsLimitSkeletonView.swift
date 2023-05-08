import SwiftUI

struct ProblemsLimitSkeletonView: View {
    private static let skeletonHeight: CGFloat = 40

    var body: some View {
        SkeletonRoundedView()
            .frame(height: Self.skeletonHeight)
    }
}

struct ProblemsLimitSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitSkeletonView()
            .padding()
    }
}
