import SwiftUI

extension StepQuizRetryButton {
    struct Appearance {
        var iconWidthHeight: CGFloat = 20
        var widthHeight: CGFloat = 44

        var tintColor = Color(ColorPalette.primary)

        var borderColor = Color(ColorPalette.primaryAlpha38)

        var backgroundColor = Color(ColorPalette.surface)
    }
}

struct StepQuizRetryButton: View {
    private(set) var appearance = Appearance()

    var onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            Image(systemName: "gobackward")
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.iconWidthHeight)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(appearance.backgroundColor)
                .addBorder(color: appearance.borderColor)
        }
        .foregroundColor(appearance.tintColor)
        .frame(widthHeight: appearance.widthHeight)
        .buttonStyle(BounceButtonStyle())
    }
}

struct StepQuizRetryButton_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizRetryButton(onTap: {})
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
