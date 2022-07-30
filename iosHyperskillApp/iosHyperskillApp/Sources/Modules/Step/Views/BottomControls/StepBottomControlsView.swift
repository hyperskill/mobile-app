import SwiftUI

struct StepBottomControlsView: View {
    var commentStatisticsViewData = [StepCommentStatisticViewData]()

    var onRatingClick: ((StepRatingControl.Rating) -> Void)?

    var onStartPracticingClick: (() -> Void)?

    var onCommentStatisticClick: ((StepCommentStatisticViewData) -> Void)?

    var body: some View {
        VStack(spacing: 48) {
            StepRatingControl(onClick: onRatingClick)

            StepActionButton(
                title: Strings.Step.startPracticing,
                style: .greenFilled,
                onClick: onStartPracticingClick
            )

            StepCommentsStatisticsView(
                viewData: commentStatisticsViewData,
                onClick: onCommentStatisticClick
            )
        }
    }
}

#if DEBUG
struct StepBottomControlsView_Previews: PreviewProvider {
    static var previews: some View {
        StepBottomControlsView(commentStatisticsViewData: StepCommentStatisticViewData.placeholders)
            .padding()
    }
}
#endif
