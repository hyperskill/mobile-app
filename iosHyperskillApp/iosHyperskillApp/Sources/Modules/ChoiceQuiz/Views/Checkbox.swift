import SwiftUI

struct Checkbox: View {
    @Binding var isSelected: Bool

    var body: some View {
        RoundedRectangle(cornerRadius: 4)
            .frame(width: 18, height: 18)
            .padding(2)
            .overlay(
                Image("choice-quiz-check-icon")
                    .renderingMode(.template)
                    .foregroundColor(Color(ColorPalette.onPrimary))
                    .background(Color(isSelected ? ColorPalette.primary : ColorPalette.onPrimary))
                    .frame(width: 12, height: 12)
            )
            .addBorder(
                color: Color(
                    isSelected ?
                    ColorPalette.primary : ColorPalette.onSurfaceAlpha60
                ),
                width: 2,
                cornerRadius: 4
            )
    }
}

struct Checkbox_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            Checkbox(isSelected: .constant(true))
            Checkbox(isSelected: .constant(false))
        }
    }
}
