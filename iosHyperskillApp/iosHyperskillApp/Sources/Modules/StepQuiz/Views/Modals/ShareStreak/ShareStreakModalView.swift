import SwiftUI

extension ShareStreakModalView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset * 2
        let actionButtonsSpacing = LayoutInsets.defaultInset

        let imageCornerRadius: CGFloat = 16
    }
}

struct ShareStreakModalView: View {
    private(set) var appearance = Appearance()

    let streak: Int

    var onShareButtonTap: (() -> Void)?
    var onNoThanksButtonTap: (() -> Void)?

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.StepQuiz.ShareStreakModal.title)
                .foregroundColor(.primaryText)
                .font(.title2)
                .bold()

            Image(uiImage: ShareStreakAction.image(for: streak))
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity)
                .cornerRadius(appearance.imageCornerRadius)

            VStack(spacing: appearance.actionButtonsSpacing) {
                Button(
                    Strings.StepQuiz.ShareStreakModal.shareButton,
                    action: { onShareButtonTap?() }
                )
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .shineEffect()

                Button(
                    Strings.StepQuiz.ShareStreakModal.noThanksButton,
                    action: { onNoThanksButtonTap?() }
                )
                .buttonStyle(OutlineButtonStyle(style: .violet))
            }
        }
        .padding()
    }
}

#Preview {
    ShareStreakModalView(streak: 1)
}
