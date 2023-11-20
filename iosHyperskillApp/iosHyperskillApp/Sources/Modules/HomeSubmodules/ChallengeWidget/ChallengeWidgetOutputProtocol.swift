import Foundation

protocol ChallengeWidgetOutputProtocol: AnyObject {
    func handleChallengeWidgetRetryContentLoading()
    func handleChallengeWidgetOpenDescriptionLink(url: URL)
    func handleChallengeWidgetDeadlineReachedReload()
    func handleChallengeWidgetCollectReward()
}
