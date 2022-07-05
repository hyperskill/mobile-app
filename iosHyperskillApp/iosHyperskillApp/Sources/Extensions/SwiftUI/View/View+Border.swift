import SwiftUI

extension View {
    func addBorder(
        color: Color = Color(ColorPalette.onSurfaceAlpha12),
        width: CGFloat = 1,
        cornerRadius: CGFloat = 8
    ) -> some View {
        addBorder(color, width: width, cornerRadius: cornerRadius)
    }

    func addBorder<S>(_ content: S, width: CGFloat, cornerRadius: CGFloat) -> some View where S: ShapeStyle {
        let roundedRect = RoundedRectangle(cornerRadius: cornerRadius)
        return clipShape(roundedRect).overlay(roundedRect.strokeBorder(content, lineWidth: width))
    }
}
