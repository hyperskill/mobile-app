import shared
import SwiftUI

extension StepQuizRunCodeFeedbackHintView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let font = UIFont.monospacedSystemFont(ofSize: 14, weight: .regular)
    }
}

struct StepQuizRunCodeFeedbackHintView: View {
    private(set) var appearance = Appearance()

    let runCodeExecution: StepQuizFeedbackStateHintFromRunCodeExecutionKs

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.StepQuiz.runCodeFeedbackHintTitle)

            switch runCodeExecution {
            case .loading:
                HStack(spacing: appearance.spacing) {
                    Text(Strings.StepQuiz.runCodeFeedbackHintCase1)
                    LinearIndeterminateProgressView()
                }
            case .result(let runCodeExecutionResult):
                Text(Strings.StepQuiz.runCodeFeedbackHintCase1)

                VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                    if let input = runCodeExecutionResult.input {
                        VStack(alignment: .leading, spacing: appearance.interitemSpacing / 2) {
                            Text(Strings.StepQuiz.runCodeFeedbackHintInput)
                            Text(input)
                        }
                    }

                    VStack(alignment: .leading, spacing: appearance.interitemSpacing / 2) {
                        Text(Strings.StepQuiz.runCodeFeedbackHintOutput)
                        Text(runCodeExecutionResult.output)
                    }
                }
            }
        }
        .font(Font(appearance.font))
        .foregroundColor(.primaryText)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color.background)
        .addBorder()
    }
}

#if DEBUG
 #Preview {
     VStack {
         StepQuizRunCodeFeedbackHintView(runCodeExecution: .loading)

         StepQuizRunCodeFeedbackHintView(
            runCodeExecution: .result(
                StepQuizFeedbackStateHintFromRunCodeExecutionResult(
                    input: "5",
                    output: "120"
                )
            )
         )
     }
     .padding()
 }
#endif
