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

    private(set) var isAlwaysExpanded = false
    @State private var isExpanded = false

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Divider()
            if isAlwaysExpanded {
                headerContent
            } else {
                Button(
                    action: {
                        withAnimation {
                            isExpanded.toggle()
                        }
                    },
                    label: {
                        headerContent
                    }
                )
            }
            Divider()

            if isExpanded || isAlwaysExpanded {
                StepQuizCodeSamplesView(
                    samples: samples,
                    executionTimeLimit: executionTimeLimit,
                    executionMemoryLimit: executionMemoryLimit
                )
            }
        }
    }

    private var headerContent: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            Image(systemName: "info.circle")
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.primaryActionButtonImageWidthHeight)

            Text(Strings.StepQuizCode.detailsTitle)
                .font(.body)

            Spacer()

            if !isAlwaysExpanded {
                Image(systemName: "chevron.down")
                    .imageScale(.small)
                    .aspectRatio(contentMode: .fit)
                    .rotationEffect(.radians(isExpanded ? .pi : .zero))
            }
        }
        .foregroundColor(.secondaryText)
        .padding()
        .background(BackgroundView())
    }
}

struct StepQuizCodeDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
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
                executionMemoryLimit: "Memory limit: 256 MB",
                isAlwaysExpanded: true
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
