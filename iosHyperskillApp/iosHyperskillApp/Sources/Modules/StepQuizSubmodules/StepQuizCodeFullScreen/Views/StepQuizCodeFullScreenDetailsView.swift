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

    let stepText: String

    let samples: [StepQuizCodeViewData.Sample]

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                CollapsableStepTextView(
                    text: stepText,
                    isCollapsed: false
                )

                StepQuizCodeDetailsView(
                    samples: samples,
                    isExpanded: true
                )
            }
            .padding()
        }
    }
}

struct StepQuizCodeFullScreenDetailsView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeFullScreenDetailsView(
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
            ]
        )
    }
}
