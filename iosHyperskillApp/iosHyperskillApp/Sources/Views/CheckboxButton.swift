import SwiftUI

extension CheckboxButton {
    struct Appearance {
        var backgroundSelectedColor = Color(ColorPalette.primary)
        var backgroundUnselectedColor = Color(ColorPalette.onPrimary)

        var checkmarkTintColor = Color(ColorPalette.onPrimary)
        var checkmarkSizeRatio: CGFloat = 0.8

        var borderWidthRatio: CGFloat = 0.1
        var borderCornerRadiusRatio: CGFloat = 0.1
        var borderSelectedColor = Color(ColorPalette.primary)
        var borderUnselectedColor = Color(ColorPalette.onSurfaceAlpha60)
    }
}

struct CheckboxButton: View {
    private(set) var appearance = Appearance()

    @Binding var isSelected: Bool

    var onClick: (() -> Void)?

    var body: some View {
        Button(
            action: { onClick?() },
            label: buildCheckbox
        )
    }

    @ViewBuilder
    private func buildCheckbox() -> some View {
        GeometryReader { proxy in
            Rectangle()
                .foregroundColor(
                    isSelected ? appearance.backgroundSelectedColor : appearance.backgroundUnselectedColor
                )
                .overlay(
                    Image(Images.StepQuiz.checkmark)
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .foregroundColor(appearance.checkmarkTintColor)
                        .frame(
                            width: proxy.size.width * appearance.checkmarkSizeRatio,
                            height: proxy.size.height * appearance.checkmarkSizeRatio
                        )
                        .opacity(isSelected ? 1 : 0)
                )
                .addBorder(
                    color: isSelected ? appearance.borderSelectedColor : appearance.borderUnselectedColor,
                    width: proxy.size.width * appearance.borderWidthRatio,
                    cornerRadius: proxy.size.width * appearance.borderCornerRadiusRatio
                )
        }
    }
}

struct CheckboxButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            CheckboxButton(isSelected: .constant(true))
                .frame(width: 50, height: 50)

            CheckboxButton(isSelected: .constant(false))
                .frame(width: 50, height: 50)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
