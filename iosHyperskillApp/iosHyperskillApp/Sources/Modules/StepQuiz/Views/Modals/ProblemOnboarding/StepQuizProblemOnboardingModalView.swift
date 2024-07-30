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

    @ViewBuilder private var graphicView: some View {
        switch modalType {
        case .parsons:
            animationView
        }
    }

    private var animationView: some View {
        let height: CGFloat = switch modalType {
        case .parsons:
            264
        }

        let fileName = switch modalType {
        case .parsons:
            LottieAnimations.parsonsProblemOnboarding
        }

        return LottieAnimationViewWrapper(
            fileName: fileName
        )
        .frame(height: height)
        .frame(maxWidth: .infinity)
    }

    private var header: String {
        switch modalType {
        case .parsons:
            Strings.StepQuiz.ProblemOnboardingModal.header
        }
    }

    private var title: String? {
        switch modalType {
        case .parsons:
            Strings.StepQuiz.ProblemOnboardingModal.title
        }
    }

    private var description: String {
        switch modalType {
        case .parsons:
            Strings.StepQuiz.ProblemOnboardingModal.parsonsDescription
        }
    }
}

#if DEBUG
#Preview {
    StepQuizProblemOnboardingModalView(modalType: .parsons)
}
#endif
