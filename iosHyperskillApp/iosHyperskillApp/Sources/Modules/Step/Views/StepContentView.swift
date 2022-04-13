import SwiftUI

struct StepContentView: View {
    let viewData: StepViewData

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                StepActionButton(
                    title: Strings.stepStartPracticingText,
                    style: .greenOutline
                ) {
                    print("Start practicing tapped")
                }

                Text(viewData.text)

                StepBottomControlsView(
                    commentStatisticsViewData: viewData.commentsStatistics,
                    onStartPracticingClick: {
                        print("Start practicing tapped")
                    },
                    onCommentStatisticClick: { commentStatistic in
                        print("Comment statistic clicked = \(commentStatistic)")
                    }
                )
            }
            .padding()
        }
    }
}

struct StepContentView_Previews: PreviewProvider {
    static var previews: some View {
        StepContentView(viewData: .placeholder)
    }
}
