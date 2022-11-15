import SkeletonUI
import SwiftUI

extension SkeletonRoundedView {
    struct Appearance {
        var cornerRadius: CGFloat = 8
    }
}

struct SkeletonRoundedView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        EmptyView()
            .skeleton(with: true)
            .shape(type: .rounded(.radius(appearance.cornerRadius)))
    }
}

struct Skeleton_Previews: PreviewProvider {
    static var previews: some View {
        SkeletonRoundedView()
            .frame(height: 100)
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
