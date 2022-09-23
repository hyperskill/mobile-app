import SwiftUI

struct StepQuizCodeSamplesView: View {
    let samples: [StepQuizCodeViewData.Sample]

    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    private var isEmpty: Bool {
        samples.isEmpty && executionTimeLimit == nil && executionMemoryLimit == nil
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            VStack(spacing: 0) {
                ForEach(samples, id: \.self) { sample in
                    StepQuizCodeSampleItemView(title: sample.inputTitle, subtitle: sample.inputValue)
                    StepQuizCodeSampleItemView(title: sample.outputTitle, subtitle: sample.outputValue)
                }

                if let executionTimeLimit = executionTimeLimit {
                    StepQuizCodeSampleItemView(
                        title: Strings.StepQuizCode.timeLimitTitle,
                        subtitle: executionTimeLimit
                    )
                }
                if let executionMemoryLimit = executionMemoryLimit {
                    StepQuizCodeSampleItemView(
                        title: Strings.StepQuizCode.memoryLimitTitle,
                        subtitle: executionMemoryLimit
                    )
                }
            }
        }
    }
}

struct StepQuizCodeSamplesView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeSamplesView(
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
