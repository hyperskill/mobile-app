import shared
import SwiftUI

struct StepQuizProblemsLimitView: View {
    let stateKs: ProblemsLimitFeatureViewStateKs

    let onReloadButtonTap: () -> Void

    var body: some View {
        if case .content(let problemsLimitContentState) = stateKs,
           case .widget = ProblemsLimitFeatureViewStateContentKs(problemsLimitContentState) {
            Divider()
        }

        ProblemsLimitView(
            stateKs: stateKs,
            onReloadButtonTap: onReloadButtonTap
        )
    }
}

struct StepQuizProblemsLimitView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitView(
            stateKs: .content(
                ProblemsLimitFeatureViewStateContentWidget(
                    stepsLimitTotal: 5,
                    stepsLimitLeft: 3,
                    stepsLimitLabel: "3/5 steps",
                    updateInLabel: "12 hours"
                )
            ),
            onReloadButtonTap: {}
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
