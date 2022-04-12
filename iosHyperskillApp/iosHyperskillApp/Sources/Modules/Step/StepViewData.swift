import Foundation
import shared

struct StepCommentStatisticViewData: Identifiable {
    let title: String
    let count: Int

    var id: String { self.title }
}

extension CommentStatisticsEntry {
    var viewData: StepCommentStatisticViewData {
        StepCommentStatisticViewData(title: self.thread, count: Int(self.totalCount))
    }
}
