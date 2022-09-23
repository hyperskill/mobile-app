import SwiftUI

extension StepQuizHintButton {
    struct Appearance {
        let interItemSpacing = LayoutInsets.smallInset

        let iconWidthHeight: CGFloat = 16

        let insets = LayoutInsets(top: 8, leading: 12, bottom: 8, trailing: 12)
    }
}

struct StepQuizHintButton: View {
    private(set) var appearance = Appearance()

    var onClick: (() -> Void)?

    var body: some View {
        Button(
            action: { onClick?() },
            label: {
                HStack(spacing: appearance.interItemSpacing) {
                    Image(Images.StepQuiz.lightning)
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(widthHeight: appearance.iconWidthHeight)

                    Text(Strings.StepQuiz.hintButton)
                        .font(.subheadline)
                }
                .padding(appearance.insets.edgeInsets)
            }
        )
        .buttonStyle(OutlineButtonStyle(font: .subheadline, maxWidth: nil))
    }
}

struct StepQuizHintButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizHintButton()

            StepQuizHintButton()
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
