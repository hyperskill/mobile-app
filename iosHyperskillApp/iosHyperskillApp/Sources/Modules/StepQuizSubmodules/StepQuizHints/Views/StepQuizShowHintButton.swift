import SwiftUI

extension StepQuizShowHintButton {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let iconWidthHeight: CGFloat = 16

        let insets = LayoutInsets(top: 8, leading: 12, bottom: 8, trailing: 12)

        let buttonMinHeight: CGFloat = 34
    }
}

struct StepQuizShowHintButton: View {
    private(set) var appearance = Appearance()

    let text: String

    var showLightning = true

    var onClick: (() -> Void)?

    var body: some View {
        Button(
            action: { onClick?() },
            label: {
                HStack(spacing: appearance.interItemSpacing) {
                    if showLightning {
                        Image(Images.StepQuiz.lightning)
                            .resizable()
                            .renderingMode(.template)
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: appearance.iconWidthHeight)
                    }

                    Text(text)
                        .font(.subheadline)
                }
                .padding(appearance.insets.edgeInsets)
            }
        )
        .buttonStyle(
            OutlineButtonStyle(
                font: .subheadline,
                minHeight: appearance.buttonMinHeight,
                maxWidth: nil
            )
        )
    }
}

struct StepQuizShowHintButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizShowHintButton(text: Strings.StepQuiz.Hints.showButton)

            StepQuizShowHintButton(text: Strings.StepQuiz.Hints.showButton)
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
