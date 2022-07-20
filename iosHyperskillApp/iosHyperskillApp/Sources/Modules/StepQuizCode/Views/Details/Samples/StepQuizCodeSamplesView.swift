import SwiftUI

struct StepQuizCodeSamplesView: View {
    let samples: [StepQuizCodeViewData.Sample]

    var body: some View {
        if samples.isEmpty {
            EmptyView()
        } else {
            VStack(spacing: 0) {
                ForEach(samples, id: \.self) { sample in
                    StepQuizCodeSampleItemView(title: sample.inputTitle, subtitle: sample.inputValue)
                    StepQuizCodeSampleItemView(title: sample.outputTitle, subtitle: sample.outputValue)
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
            ]
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
