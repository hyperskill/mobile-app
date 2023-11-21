import Foundation
import shared

extension ChallengeWidgetViewStateContentCollectRewardButtonStateKs: Equatable {
    public static func == (
        lhs: ChallengeWidgetViewStateContentCollectRewardButtonStateKs,
        rhs: ChallengeWidgetViewStateContentCollectRewardButtonStateKs
    ) -> Bool {
        switch (lhs, rhs) {
        case (.hidden, .hidden):
            true
        case (.visible(let lhsData), .visible(let rhsData)):
            lhsData.isEqual(rhsData)
        case (.visible, .hidden):
            false
        case (.hidden, .visible):
            false
        }
    }
}
