import SwiftUI

struct StepQuizCodeDetailsView: View {
    let samples: [StepQuizCodeViewData.Sample]

    var body: some View {
        StepQuizCodeSamplesView(samples: samples)
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
            ]
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
