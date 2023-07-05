import SwiftUI

extension StageImplementProjectCompletedModalView {
    struct Appearance {
        let spacing: CGFloat = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.defaultInset
    }
}

struct StageImplementProjectCompletedModalView: View {
    private(set) var appearance = Appearance()

    let stageAward: Int
    let projectAward: Int

    var onCallToActionTap: () -> Void = {}

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Image(Images.StageImplement.ProjectCompletedModal.icon)

            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(Strings.StageImplement.ProjectCompletedModal.title)
                    .font(.title2).bold()
                    .foregroundColor(.primaryText)

                Text(Strings.StageImplement.ProjectCompletedModal.description)
                    .font(.body)
                    .foregroundColor(.primaryText)

                HypercoinsAwardView
                    .stageCompleted(award: stageAward)
                HypercoinsAwardView
                    .projectCompleted(award: projectAward)
            }

            Button(
                Strings.General.goToStudyPlan,
                action: onCallToActionTap
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
        .padding([.horizontal, .bottom])
    }
}

struct StageImplementProjectCompletedModalView_Previews: PreviewProvider {
    static var previews: some View {
        StageImplementProjectCompletedModalView(
            stageAward: 15,
            projectAward: 25,
            onCallToActionTap: {}
        )

        StageImplementProjectCompletedModalView(
            stageAward: 15,
            projectAward: 25,
            onCallToActionTap: {}
        )
        .preferredColorScheme(.dark)
    }
}
