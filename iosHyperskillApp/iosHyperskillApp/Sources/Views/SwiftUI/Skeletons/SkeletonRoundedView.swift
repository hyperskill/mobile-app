import SkeletonUI
import SwiftUI

extension SkeletonRoundedView {
    struct Appearance {
        var size: CGSize?
        var cornerRadius: CGFloat = 8
    }
}

struct SkeletonRoundedView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        EmptyView()
            .skeleton(with: true, size: appearance.size)
            .shape(type: .rounded(.radius(appearance.cornerRadius)))
            .appearance(type: .gradient())
            .animation(type: .linear(autoreverses: true))
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
