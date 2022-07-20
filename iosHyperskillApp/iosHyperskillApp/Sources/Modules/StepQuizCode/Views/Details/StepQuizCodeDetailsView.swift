import SwiftUI

extension StepQuizCodeDetailsView {
    struct Appearance {
        let primaryActionButtonImageWidthHeight: CGFloat = 20
    }
}

struct StepQuizCodeDetailsView: View {
    private(set) var appearance = Appearance()

    let samples: [StepQuizCodeViewData.Sample]

    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    @State private var isExpanded = false

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Divider()
            primaryActionButton
            Divider()

            if isExpanded {
                StepQuizCodeSamplesView(
                    samples: samples,
                    executionTimeLimit: executionTimeLimit,
                    executionMemoryLimit: executionMemoryLimit
                )
            }
        }
    }

    private var primaryActionButton: some View {
        Button(
            action: {
                withAnimation {
                    isExpanded.toggle()
                }
            },
            label: {
                HStack(spacing: LayoutInsets.defaultInset) {
                    Image(systemName: "info.circle")
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(widthHeight: appearance.primaryActionButtonImageWidthHeight)

                    Text(Strings.StepQuizCode.detailsTitle)
                        .font(.body)

                    Spacer()

                    Image(systemName: "chevron.down")
                        .imageScale(.small)
                        .aspectRatio(contentMode: .fit)
                        .rotationEffect(.radians(isExpanded ? .pi : .zero))
                }
                .foregroundColor(.secondaryText)
                .padding()
                .background(BackgroundView())
            }
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
