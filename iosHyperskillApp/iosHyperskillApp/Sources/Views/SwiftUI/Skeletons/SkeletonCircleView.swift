import SkeletonUI
import SwiftUI

extension SkeletonCircleView {
    struct Appearance {
        var size: CGSize?
    }
}

struct SkeletonCircleView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        EmptyView()
            .skeleton(with: true, size: appearance.size)
            .shape(type: .circle)
            .appearance(type: .gradient())
            .animation(type: .linear(autoreverses: true))
    }
}

struct SkeletonCircleView_Previews: PreviewProvider {
    static var previews: some View {
        SkeletonCircleView(appearance: .init(size: .init(width: 44, height: 44)))
    }
}
