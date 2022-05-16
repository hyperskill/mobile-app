import SwiftUI

struct RadioButton: View {
    @Binding var isSelected: Bool

    var body: some View {
        Circle()
            .frame(width: 24, height: 24)
            .foregroundColor(Color(ColorPalette.onPrimary))
            .addBorder(
                color: Color(
                    isSelected ?
                    ColorPalette.primary : ColorPalette.onSurfaceAlpha60
                ),
                width: 2,
                cornerRadius: 24
            )
            .overlay(
                Circle()
                    .foregroundColor(
                        Color(
                            isSelected
                            ? ColorPalette.primary
                            : ColorPalette.onPrimary
                        )
                    )
                    .frame(width: 12, height: 12)
            )
    }
}

struct RadioButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            RadioButton(isSelected: .constant(true))
            RadioButton(isSelected: .constant(false))
        }
    }
}
