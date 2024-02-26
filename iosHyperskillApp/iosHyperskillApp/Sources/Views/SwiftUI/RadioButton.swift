import SwiftUI

extension RadioButton {
    struct Appearance {
        var borderWidth: CGFloat = 2
        var borderSelectedColor = Color(ColorPalette.primary)
        var borderUnselectedColor = Color(ColorPalette.onSurfaceAlpha60)

        var indicatorSelectedColor = Color(ColorPalette.primary)
        var indicatorUnselectedColor = Color(ColorPalette.onPrimary)

        var backgroundColor = Color(ColorPalette.onPrimary)
    }
}

struct RadioButton: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool

    var onClick: (() -> Void)?

    var body: some View {
        Button(
            action: { onClick?() },
            label: buildRadioCircle
        )
    }

    @ViewBuilder
    private func buildRadioCircle() -> some View {
        GeometryReader { proxy in
            Circle()
                .foregroundColor(appearance.backgroundColor)
                .addBorder(
                    color: isSelected ? appearance.borderSelectedColor : appearance.borderUnselectedColor,
                    width: appearance.borderWidth,
                    cornerRadius: max(proxy.size.width, proxy.size.height) / 2
                )
                .overlay(
                    Circle()
                        .frame(width: proxy.size.width / 2, height: proxy.size.height / 2)
                        .foregroundColor(
                            isSelected ? appearance.indicatorSelectedColor : appearance.indicatorUnselectedColor
                        )
                )
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        RadioButton(isSelected: true)
            .frame(width: 24, height: 24)

        RadioButton(isSelected: false)
            .frame(width: 24, height: 24)
    }
    .padding()
}
#endif
