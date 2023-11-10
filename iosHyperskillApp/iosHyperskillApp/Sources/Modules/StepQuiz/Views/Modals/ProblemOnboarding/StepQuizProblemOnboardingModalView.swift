import shared
import SwiftUI

struct StepQuizProblemOnboardingModalView: View {
    let modalType: StepQuizFeatureProblemOnboardingModalKs

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            Text(Strings.StepQuiz.ProblemOnboardingModal.header)
                .font(.title2).bold()
                .foregroundColor(.primaryText)

            animationView

            Text(Strings.StepQuiz.ProblemOnboardingModal.title)
                .font(.headline)
                .foregroundColor(.primaryText)

            Text(description)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
        .padding()
    }

    private var animationView: some View {
        let height: CGFloat = switch modalType {
        case .parsons:
            264
        case .fillBlanks(let data):
            switch FillBlanksModeWrapper(shared: data.mode).require() {
            case .input:
                110
            case .select:
                198
            }
        }

        let fileName = switch modalType {
        case .parsons:
            LottieAnimations.parsonsProblemOnboarding
        case .fillBlanks(let data):
            switch FillBlanksModeWrapper(shared: data.mode).require() {
            case .input:
                LottieAnimations.fillBlanksInputProblemOnboarding
            case .select:
                LottieAnimations.fillBlanksSelectProblemOnboarding
            }
        }

        return LottieAnimationViewWrapper(
            fileName: fileName
        )
        .frame(height: height)
        .frame(maxWidth: .infinity)
    }

    private var description: String {
        switch modalType {
        case .parsons:
            Strings.StepQuiz.ProblemOnboardingModal.parsonsDescription
        case .fillBlanks:
            Strings.StepQuiz.ProblemOnboardingModal.fillBlanksDescription
        }
    }
}

#Preview {
    StepQuizProblemOnboardingModalView(modalType: .parsons)
}

#Preview {
    StepQuizProblemOnboardingModalView(
        modalType: .fillBlanks(StepQuizFeatureProblemOnboardingModalFillBlanks(mode: .input))
    )
}

#Preview {
    StepQuizProblemOnboardingModalView(
        modalType: .fillBlanks(StepQuizFeatureProblemOnboardingModalFillBlanks(mode: .select))
    )
}
