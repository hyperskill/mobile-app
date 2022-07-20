import SwiftUI

extension StepQuizCodeSamplesView {
    struct Appearance {
        let textFont = Font(UIFont.monospacedSystemFont(ofSize: 14, weight: .regular))
    }
}

struct StepQuizCodeSamplesView: View {
    private(set) var appearance = Appearance()

    let samples: [StepQuizCodeViewData.Sample]

    var body: some View {
        if samples.isEmpty {
            EmptyView()
        } else {
            VStack(spacing: 0) {
                ForEach(samples, id: \.self) { sample in
                    buildItemView(title: sample.inputTitle, subtitle: sample.inputValue)
                    buildItemView(title: sample.outputTitle, subtitle: sample.outputValue)
                }
            }
        }
    }

    @ViewBuilder
    private func buildItemView(title: String, subtitle: String) -> some View {
        VStack(alignment: .leading, spacing: 0) {
            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                Text(title)
                    .font(appearance.textFont)
                    .foregroundColor(.secondaryText)

                Text(subtitle)
                    .font(appearance.textFont)
                    .foregroundColor(.primaryText)
            }
            .padding()

            Divider()
        }
        .background(BackgroundView())
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
