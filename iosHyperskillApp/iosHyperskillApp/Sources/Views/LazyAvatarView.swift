import Nuke
import NukeUI
import SkeletonUI
import SwiftUI

extension LazyAvatarView {
    struct Appearance {
        var borderColor = Color(ColorPalette.onSurfaceAlpha12)
        var borderWidth: CGFloat = 1
    }
}

struct LazyAvatarView: View {
    private(set) var appearance = Appearance()

    private let source: ImageRequestConvertible?

    @State private var isLoading = false

    init(_ source: String?) {
        self.source = source
    }

    init(_ source: URL?) {
        self.source = source
    }

    init(_ source: URLRequest?) {
        self.source = source
    }

    var body: some View {
        GeometryReader { geometryProxy in
            // Using additional view here because skeleton modifier not working with LazyImage
            ZStack {
                LazyImage(
                    source: source,
                    resizingMode: .aspectFill
                )
                .onStart { _ in
                    isLoading = true
                }
                .onCompletion { _ in
                    isLoading = false
                }

                EmptyView()
                    .skeleton(with: isLoading)
            }
            .addBorder(
                color: appearance.borderColor,
                width: appearance.borderWidth,
                cornerRadius: max(geometryProxy.size.width, geometryProxy.size.height) * 2
            )
        }
    }
}

struct ProfileAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            LazyAvatarView("")
                .frame(widthHeight: 64)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
