import SwiftUI

extension InterviewPreparationCompletedModalView {
    struct Appearance {
        let spacing: CGFloat = LayoutInsets.defaultInset * 2
        let interitemSpacing = LayoutInsets.defaultInset
    }
}

struct InterviewPreparationCompletedModalView: View {
    private(set) var appearance = Appearance()

    var onCallToActionTap: () -> Void = {}

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Image(.stageImplementStageCompletedModalIcon)
                .renderingMode(.original)
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(Strings.InterviewPreparationCompletedModal.title)
                    .font(.title2).bold()
                    .foregroundColor(.primaryText)

                Text(Strings.InterviewPreparationCompletedModal.description)
                    .font(.body)
                    .foregroundColor(.primaryText)
            }

            Button(
                Strings.Common.goToTraining,
                action: onCallToActionTap
            )
            .buttonStyle(.primary)
            .shineEffect()
        }
        .padding([.horizontal, .bottom])
    }
}

#Preview {
    InterviewPreparationCompletedModalView()
}
