import SwiftUI

extension StepTheoryContentView {
    struct Appearance {
        let interItemSpacing: CGFloat = 24
    }
}

struct StepTheoryContentView: View {
    private(set) var appearance = Appearance()

    @State var viewData: StepViewData

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                // ALTAPPS-397: Hidden
//                StepActionButton(
//                    title: Strings.Step.startPracticing,
//                    style: .greenOutline
//                ) {
//                    print("Start practicing tapped")
//                }

                StepTextView(text: viewData.text)

                // ALTAPPS-397: Hidden
//                StepBottomControlsView(
//                    commentStatisticsViewData: viewData.commentsStatistics,
//                    onStartPracticingClick: {
//                        print("Start practicing tapped")
//                    },
//                    onCommentStatisticClick: { commentStatistic in
//                        print("Comment statistic clicked = \(commentStatistic)")
//                    }
//                )
            }
            .padding()
        }
    }
}

#if DEBUG
struct StepContentView_Previews: PreviewProvider {
    static var previews: some View {
        StepTheoryContentView(viewData: .placeholder)
    }
}
#endif
