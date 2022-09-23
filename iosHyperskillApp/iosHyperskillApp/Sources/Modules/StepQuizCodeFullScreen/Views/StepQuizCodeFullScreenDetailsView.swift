import SwiftUI

extension StepQuizCodeFullScreenDetailsView {
    struct Appearance {
        let stepTextFont = UIFont.preferredFont(forTextStyle: .subheadline)
        let stepTextColor = UIColor.primaryText

        let spacing = LayoutInsets.defaultInset
    }
}

struct StepQuizCodeFullScreenDetailsView: View {
    private(set) var appearance = Appearance()

    let stepStats: String
    let stepText: String

    let samples: [StepQuizCodeViewData.Sample]
    let executionTimeLimit: String?
    let executionMemoryLimit: String?

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                StepQuizStatsView(text: stepStats)

                LatexView(
                    text: .constant(stepText),
                    configuration: .init(
                        appearance: .init(labelFont: appearance.stepTextFont),
                        contentProcessor: ContentProcessor(
                            injections: ContentProcessor.defaultInjections + [
                                StepStylesInjection(),
                                FontInjection(font: appearance.stepTextFont),
                                TextColorInjection(dynamicColor: appearance.stepTextColor)
                            ]
                        )
                    )
                )

                StepQuizCodeDetailsView(
                    samples: samples,
                    executionTimeLimit: executionTimeLimit,
                    executionMemoryLimit: executionMemoryLimit,
                    isAlwaysExpanded: true
                )
                .padding(.horizontal, -appearance.spacing)
            }
            .padding()
        }
    }
}

struct StepQuizCodeFullScreenDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenDetailsView(
            stepStats: "2438 users solved this problem. Latest completion was about 13 hours ago.",
            stepText: """
Enter only the name of the found functional interface with/without the package. Don't write any generic parameters.
""",
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
    }
}
