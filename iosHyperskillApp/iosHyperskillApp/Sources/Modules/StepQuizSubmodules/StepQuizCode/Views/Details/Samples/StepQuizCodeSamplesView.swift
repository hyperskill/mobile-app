import SwiftUI

struct StepQuizCodeSamplesView: View {
    let samples: [StepQuizCodeViewData.Sample]

    var body: some View {
        VStack(spacing: 0) {
            ForEach(Array(zip(samples.indices, samples)), id: \.0) { index, sample in
                StepQuizCodeSampleItemView(
                    title: sample.inputTitle,
                    subtitle: sample.inputValue
                )

                Divider()
                    .padding(.horizontal)

                StepQuizCodeSampleItemView(
                    title: sample.outputTitle,
                    subtitle: sample.outputValue
                )

                if index != samples.endIndex - 1 {
                    Divider()
                        .padding(.horizontal)
                }
            }
        }
        .addBorder()
    }
}

#Preview {
    StepQuizCodeSamplesView(
        samples: [
            .init(
                inputTitle: "Sample Input 1",
                inputValue: "3\n3\n3",
                outputTitle: "Sample Output 1",
                outputValue: "true"
            ),
            .init(
                inputTitle: "Sample Input 2",
                inputValue: "3\n3\n3",
                outputTitle: "Sample Output 2",
                outputValue: "true"
            )
        ]
    )
    .padding()
}
