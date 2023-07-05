import SwiftUI

extension StageImplementStageCompletedModalView {
    struct Appearance {
        let spacing: CGFloat = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.defaultInset

        let hypercoinIconSize = CGSize(width: 28, height: 28)
    }
}

struct StageImplementStageCompletedModalView: View {
    private(set) var appearance = Appearance()

    let title: String
    let award: Int

    var onCallToActionTap: () -> Void = {}

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Image(Images.StageImplement.StageCompletedModal.icon)

            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(title)
                    .font(.title2).bold()
                    .foregroundColor(.primaryText)

                Text(Strings.StageImplement.StageCompletedModal.description)
                    .font(.body)
                    .foregroundColor(.primaryText)

                HypercoinLabel(
                    title: {
                        Text("+\(award)")
                            .font(.subheadline)
                            .foregroundColor(Color(ColorPalette.overlayViolet)) +
                        Text(" \(Strings.StageImplement.StageCompletedModal.awardDescription)")
                            .font(.subheadline)
                            .foregroundColor(.primaryText)
                    },
                    appearance: .init(iconSize: appearance.hypercoinIconSize)
                )
            }
            .frame(maxWidth: .infinity)

            Button(
                Strings.General.goToStudyPlan,
                action: onCallToActionTap
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
        .padding([.horizontal, .bottom])
    }
}

struct StageImplementStageCompletedModalView_Previews: PreviewProvider {
    static var previews: some View {
        StageImplementStageCompletedModalView(
            title: "Yay! You've completed Stage 1/10!",
            award: 15,
            onCallToActionTap: {}
        )

        StageImplementStageCompletedModalView(
            title: "Yay! You've completed Stage 1/10!",
            award: 15,
            onCallToActionTap: {}
        )
        .preferredColorScheme(.dark)
    }
}
