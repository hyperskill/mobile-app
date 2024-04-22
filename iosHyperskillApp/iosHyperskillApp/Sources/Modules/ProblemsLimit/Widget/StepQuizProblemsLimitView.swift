import shared
import SwiftUI

struct StepQuizProblemsLimitView: View {
    let stateKs: LegacyProblemsLimitFeatureViewStateKs

    let onReloadButtonTap: () -> Void

    var body: some View {
        if case .content(let problemsLimitContentState) = stateKs,
           case .widget = LegacyProblemsLimitFeatureViewStateContentKs(problemsLimitContentState) {
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
                LegacyProblemsLimitFeatureViewStateContentWidget(
                    progress: 0.6,
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
