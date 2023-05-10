import shared
import SwiftUI

struct ProblemsLimitView: View {
    let stateKs: ProblemsLimitFeatureViewStateKs

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
            switch ProblemsLimitFeatureViewStateContentKs(content) {
            case .empty:
                EmptyView()
            case .widget(let data):
                ProblemsLimitWidgetView(
                    stepsLimitLeft: Int(data.stepsLimitLeft),
                    stepsLimitTotal: Int(data.stepsLimitTotal),
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
