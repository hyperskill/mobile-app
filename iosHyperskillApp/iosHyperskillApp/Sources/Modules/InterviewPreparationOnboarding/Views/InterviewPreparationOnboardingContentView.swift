import SwiftUI

extension InterviewPreparationOnboardingContentView {
    struct Appearance {
        let headerTextInteritemSpacing = LayoutInsets.smallInset

        let illustrationMaxWidth: CGFloat = 200
        let illustrationMaxHeight: CGFloat = 184
    }
}

struct InterviewPreparationOnboardingContentView: View {
    private(set) var appearance = Appearance()

    let onCallToActionButtonTap: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            VStack(alignment: .center, spacing: appearance.headerTextInteritemSpacing) {
                Text(Strings.InterviewPreparationOnboarding.title)
                    .font(.title).bold()
                    .foregroundColor(.newPrimaryText)

                Text(Strings.InterviewPreparationOnboarding.description)
                    .font(.body)
                    .foregroundColor(.newSecondaryText)
            }
            .multilineTextAlignment(.center)
            .padding(.vertical)

            Image(
                ImageResource.interviewPreparationOnboardingIllustration
            )
            .renderingMode(.original)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(
                maxWidth: appearance.illustrationMaxWidth,
                maxHeight: appearance.illustrationMaxHeight
            )
            .padding(.vertical)

            Spacer()

            Button(
                Strings.InterviewPreparationOnboarding.callToActionButton,
                action: {
                    FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                    onCallToActionButtonTap()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()
        }
        .padding()
        .frame(maxWidth: .infinity)
    }
}

#Preview {
    InterviewPreparationOnboardingContentView(
        onCallToActionButtonTap: {}
    )
}
