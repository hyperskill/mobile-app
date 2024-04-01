import shared
import SwiftUI

struct StepQuizProblemOnboardingModalView: View {
    let modalType: StepQuizFeatureProblemOnboardingModalKs

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            Text(header)
                .font(.title2).bold()
                .foregroundColor(.primaryText)

            graphicView

            if let title {
                Text(title)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            }

            Text(description)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
        .padding()
    }

    @ViewBuilder
    private var graphicView: some View {
        switch modalType {
        case .gptCodeGenerationWithErrors:
            Image(.problemOnboardingGptCodeGenerationWithErrors)
                .renderingMode(.original)
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, alignment: .center)
        default:
            animationView
        }
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
        case .gptCodeGenerationWithErrors:
            fatalError("Did receive unsupported modal type")
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
        case .gptCodeGenerationWithErrors:
            fatalError("Did receive unsupported modal type")
        }

        return LottieAnimationViewWrapper(
            fileName: fileName
        )
        .frame(height: height)
        .frame(maxWidth: .infinity)
    }

    private var header: String {
        switch modalType {
        case .gptCodeGenerationWithErrors:
            Strings.StepQuiz.ProblemOnboardingModal.gptCodeGenerationWithErrorsHeader
        default:
            Strings.StepQuiz.ProblemOnboardingModal.header
        }
    }

    private var title: String? {
        switch modalType {
        case .gptCodeGenerationWithErrors:
            nil
        default:
            Strings.StepQuiz.ProblemOnboardingModal.title
        }
    }

    private var description: String {
        switch modalType {
        case .parsons:
            Strings.StepQuiz.ProblemOnboardingModal.parsonsDescription
        case .fillBlanks:
            Strings.StepQuiz.ProblemOnboardingModal.fillBlanksDescription
        case .gptCodeGenerationWithErrors:
            Strings.StepQuiz.ProblemOnboardingModal.gptCodeGenerationWithErrorsDescription
        }
    }
}

#if DEBUG
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

#Preview {
    StepQuizProblemOnboardingModalView(modalType: .gptCodeGenerationWithErrors)
}
#endif
