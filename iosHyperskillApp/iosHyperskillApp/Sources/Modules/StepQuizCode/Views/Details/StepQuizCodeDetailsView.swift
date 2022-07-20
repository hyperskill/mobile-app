import SwiftUI

struct StepQuizCodeDetailsView: View {
    let samples: [StepQuizCodeViewData.Sample]

    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    var body: some View {
        StepQuizCodeSamplesView(
            samples: samples,
            executionTimeLimit: executionTimeLimit,
            executionMemoryLimit: executionMemoryLimit
        )
    }
}

struct StepQuizCodeDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeDetailsView(
            samples: [
                .init(
                    inputTitle: "Sample Input 1",
                    inputValue: "3\n3\n3",
                    outputTitle: "Sample Output 1",
                    outputValue: "true"
                )
            ],
            executionTimeLimit: "Time limit: 8 seconds",
            executionMemoryLimit: "Memory limit: 256 MB"
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
