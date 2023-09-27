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

    let onExpandStepTextButtonTap: () -> Void
    let onExpandCodeDetailsButtonTap: () -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                ExpandableStepTextView(
                    text: stepText,
                    isExpanded: true,
                    onExpandButtonTap: onExpandStepTextButtonTap
                )

                StepQuizCodeDetailsView(
                    samples: samples,
                    isExpanded: true,
                    onExpandTapped: onExpandCodeDetailsButtonTap
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
            ],
            onExpandStepTextButtonTap: {},
            onExpandCodeDetailsButtonTap: {}
        )
    }
}
