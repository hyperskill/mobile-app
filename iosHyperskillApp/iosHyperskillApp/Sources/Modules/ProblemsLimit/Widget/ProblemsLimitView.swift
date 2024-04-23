import shared
import SwiftUI

@available(*, deprecated, message: "ALTAPPS-1226: Delete this")
struct ProblemsLimitView: View {
    let stateKs: LegacyProblemsLimitFeatureViewStateKs

    let onReloadButtonTap: () -> Void

    var body: some View {
        switch stateKs {
        case .idle:
            EmptyView()
        case .loading:
            ProblemsLimitSkeletonView()
        case .error:
            Button(
                Strings.Placeholder.networkErrorButtonText,
                action: onReloadButtonTap
            )
            .buttonStyle(OutlineButtonStyle())
        case .content(let content):
            switch LegacyProblemsLimitFeatureViewStateContentKs(content) {
            case .empty:
                EmptyView()
            case .widget(let data):
                ProblemsLimitWidgetView(
                    stepsLimitProgress: data.progress,
                    stepsLimitLabel: data.stepsLimitLabel,
                    updateInLabel: data.updateInLabel
                )
            }
        }
    }
}

struct ProblemsLimitView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitView(
            stateKs: .content(
                LegacyProblemsLimitFeatureViewStateContentWidget(
                    progress: 0.6,
                    stepsLimitLabel: "3/5 steps",
                    updateInLabel: "Update in 12 hours"
                )
            ),
            onReloadButtonTap: {}
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
