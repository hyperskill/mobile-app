import SwiftUI

struct StepQuizParsonsOnboardingModalView: View {
    private static let animationViewHeight: CGFloat = 264

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            Text(Strings.StepQuizParsons.OnboardingModal.header)
                .font(.title2).bold()
                .foregroundColor(.primaryText)

            LottieAnimationViewWrapper(
                fileName: LottieAnimations.parsonsProblemOnboarding
            )
            .frame(height: Self.animationViewHeight)
            .frame(maxWidth: .infinity)

            Text(Strings.StepQuizParsons.OnboardingModal.title)
                .font(.headline)
                .foregroundColor(.primaryText)

            Text(Strings.StepQuizParsons.OnboardingModal.description)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
        .padding()
    }
}

struct StepQuizParsonsOnboardingModalView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizParsonsOnboardingModalView()
    }
}
