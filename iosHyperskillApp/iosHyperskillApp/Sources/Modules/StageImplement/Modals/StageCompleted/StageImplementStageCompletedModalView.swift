import SwiftUI

extension StageImplementStageCompletedModalView {
    struct Appearance {
        let spacing: CGFloat = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.defaultInset
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

                HypercoinsAwardView
                    .stageCompleted(award: award)
            }

            Button(
                Strings.Common.goToStudyPlan,
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
