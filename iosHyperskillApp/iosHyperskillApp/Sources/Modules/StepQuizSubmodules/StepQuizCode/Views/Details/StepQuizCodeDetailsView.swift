import SwiftUI

struct StepQuizCodeDetailsView: View {
    let samples: [StepQuizCodeViewData.Sample]

    @State var isExpanded = false

    var onExpandTapped: (() -> Void)?

    var body: some View {
        if samples.isEmpty {
            EmptyView()
        } else {
            contentView
        }
    }

    private var contentView: some View {
        VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
            Button(
                action: {
                    onExpandTapped?()

                    withAnimation {
                        isExpanded.toggle()
                    }
                },
                label: {
                    HStack(alignment: .center) {
                        Text(Strings.StepQuizCode.detailsTitle)
                            .foregroundColor(.primaryText)
                            .frame(maxWidth: .infinity, alignment: .leading)

                        Spacer()

                        Image(systemName: "chevron.right")
                            .imageScale(.small)
                            .aspectRatio(contentMode: .fit)
                            .rotationEffect(.radians(isExpanded ? (.pi / 2) : .zero))
                    }
                    .font(.headline)
                }
            )

            if isExpanded {
                StepQuizCodeSamplesView(
                    samples: samples
                )
            }
        }
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
                isExpanded: false
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
                isExpanded: true
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
