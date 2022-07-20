import SwiftUI

extension Skeleton {
    struct Appearance {
        let cornerRadius: CGFloat = 8
    }
}

struct Skeleton: View {
    private(set) var appearance = Appearance()

    var body: some View {
        EmptyView()
            .skeleton(with: true)
            .shape(type: .rectangle)
            .cornerRadius(appearance.cornerRadius)
    }
}

struct Skeleton_Previews: PreviewProvider {
    static var previews: some View {
        Skeleton()
    }
}
