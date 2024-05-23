import SwiftUI

struct SkeletonRoundedButton: View {
    var height: CGFloat = 44

    var body: some View {
        SkeletonRoundedView()
            .frame(height: height)
    }
}

struct SkeletonRoundedButton_Previews: PreviewProvider {
    static var previews: some View {
        SkeletonRoundedButton()
    }
}
