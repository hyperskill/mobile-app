import SwiftUI

struct StepQuizRunCodeFeedbackHintView: View {
    let runCodeExecution: StepQuizFeedbackStateHintFromRunCodeExecutionKs

    var body: some View {
        switch runCodeExecution {
        case .loading:
            Text("loading")
        case .result(let runCodeExecutionResult):
            Text("\(runCodeExecutionResult)")
        }
    }
}

// #Preview {
//    StepQuizRunCodeFeedbackHintView()
// }
