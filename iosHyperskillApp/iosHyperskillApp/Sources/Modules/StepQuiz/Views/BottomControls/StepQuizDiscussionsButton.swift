import SwiftUI

extension StepQuizDiscussionsButton {
    struct Appearance {
        var minHeight: CGFloat = 44

        var iconWidthHeight: CGFloat = 20
        var iconLayoutInsets = LayoutInsets(left: 18)
        var iconTintColor = Color(ColorPalette.primary)
    }
}

struct StepQuizDiscussionsButton: View {
    private(set) var appearance = Appearance()

    var onClick: (() -> Void)?

    var body: some View {
        Button(
            action: { self.onClick?() },
            label: {
                Text(Strings.choiceQuizDiscussionsButtonText)
                    .font(.body)
                    .frame(maxWidth: .infinity, minHeight: appearance.minHeight)
                    .overlay(
                        Image(Images.StepQuiz.discussions)
                            .resizable()
                            .renderingMode(.template)
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: appearance.iconWidthHeight)
                            .padding(appearance.iconLayoutInsets.edgeInsets)
                            .foregroundColor(appearance.iconTintColor),
                        alignment: .leading
                    )
            }
        )
        .buttonStyle(OutlineButtonStyle())
    }
}

struct StepQuizDiscussionsButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizDiscussionsButton()

            StepQuizDiscussionsButton()
                .preferredColorScheme(.dark)
        }
        .padding()
    }
}
