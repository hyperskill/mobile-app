import shared
import SwiftUI

extension ProblemsLimitContent {
    struct Appearance {
        var showTopDivider = false

        let skeletonHeight: CGFloat = 40
    }
}

struct ProblemsLimitContent: View {
    private(set) var appearance = Appearance()

    let stateKs: ProblemsLimitFeatureViewStateKs

    var body: some View {
        switch stateKs {
        case .idle, .loading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHeight)
        case .error:
            EmptyView()
        case .content(let content):
            switch ProblemsLimitFeatureViewStateContentKs(content) {
            case .empty:
                EmptyView()
            case .widget(let data):
                if appearance.showTopDivider {
                    Divider()
                }

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
        ProblemsLimitContent(
            stateKs: .content(
                ProblemsLimitFeatureViewStateContentWidget(
                    stepsLimitTotal: 5,
                    stepsLimitLeft: 3,
                    stepsLimitLabel: "3/5 steps",
                    updateInLabel: "12 hours"
                )
            )
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
