import SwiftUI

extension View {
    func frame(widthHeight: CGFloat) -> some View {
        frame(width: widthHeight, height: widthHeight)
    }

    func frame(size: CGSize) -> some View {
        frame(width: size.width, height: size.height)
    }
}
