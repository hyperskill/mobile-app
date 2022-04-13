import SwiftUI

struct StepBottomControlsView: View {
    var commentStatisticsViewData = [StepCommentStatisticViewData]()

    var onRatingClick: ((StepRatingControl.Rating) -> Void)?

    var onStartPracticingClick: (() -> Void)?

    var onCommentStatisticClick: ((StepCommentStatisticViewData) -> Void)?

    var body: some View {
        VStack(spacing: 48) {
            StepRatingControl(onClick: self.onRatingClick)

            StepActionButton(
                title: Strings.stepStartPracticingText,
                style: .greenFilled,
                onClick: self.onStartPracticingClick
            )

            StepCommentsStatisticsView(
                viewData: self.commentStatisticsViewData,
                onClick: self.onCommentStatisticClick
            )
        }
    }
}

struct StepBottomControlsView_Previews: PreviewProvider {
    static var previews: some View {
        StepBottomControlsView(commentStatisticsViewData: StepCommentStatisticViewData.placeholders)
            .padding()
    }
}
