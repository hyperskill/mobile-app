import SwiftUI

struct StepCommentsStatisticsView: View {
    var viewData = [StepCommentStatisticViewData]()

    var onClick: ((StepCommentStatisticViewData) -> Void)?

    var body: some View {
        VStack {
            ForEach(viewData) { data in
                StepActionButton(
                    title: data.title,
                    style: .violetOutline
                ) {
                    onClick?(data)
                }
            }
        }
    }
}

#if DEBUG
struct StepCommentsStatisticsView_Previews: PreviewProvider {
    static var previews: some View {
        StepCommentsStatisticsView(viewData: StepCommentStatisticViewData.placeholders)
            .padding()
    }
}
#endif
