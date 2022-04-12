import SwiftUI

struct StepCommentsStatisticsView: View {
    var viewData = [StepCommentStatisticViewData]()

    var onClick: ((StepCommentStatisticViewData) -> Void)?

    var body: some View {
        VStack {
            ForEach(viewData) { data in
                StepActionButton(
                    title: "\(data.title) (\(data.count))",
                    style: .violetOutline
                ) {
                    self.onClick?(data)
                }
            }
        }
    }
}

struct StepCommentsStatisticsView_Previews: PreviewProvider {
    static var previews: some View {
        StepCommentsStatisticsView(
            viewData: [
                .init(title: "Comments", count: 0),
                .init(title: "Hints", count: 0),
                .init(title: "Useful links", count: 0),
                .init(title: "Solutions", count: 0)
            ]
        )
        .padding()
    }
}
