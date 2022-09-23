import SwiftUI

extension View {
    func frame(widthHeight: CGFloat, alignment: Alignment = .center) -> some View {
        frame(width: widthHeight, height: widthHeight, alignment: alignment)
    }

    func frame(size: CGSize, alignment: Alignment = .center) -> some View {
        frame(width: size.width, height: size.height, alignment: alignment)
    }
}
