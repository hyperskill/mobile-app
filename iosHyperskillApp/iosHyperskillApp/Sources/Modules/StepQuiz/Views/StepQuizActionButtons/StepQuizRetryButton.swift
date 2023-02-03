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

    var style: Style

    var onTap: () -> Void

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        switch style {
        case .logoOnly:
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
            .opacity(isEnabled ? 1 : 0.38)
        case .roundedRectangle:
            Button(
                Strings.StepQuiz.retryButton,
                action: onTap
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }

    enum Style {
        case logoOnly
        case roundedRectangle
    }
}

struct StepQuizRetryButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            HStack {
                StepQuizRetryButton(style: .logoOnly, onTap: {})

                StepQuizRetryButton(style: .logoOnly, onTap: {})
                    .disabled(true)
            }

            StepQuizRetryButton(style: .roundedRectangle, onTap: {})

            StepQuizRetryButton(style: .roundedRectangle, onTap: {})
                .disabled(true)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
