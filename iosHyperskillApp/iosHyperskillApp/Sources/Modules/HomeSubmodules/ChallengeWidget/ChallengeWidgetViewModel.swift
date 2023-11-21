import Foundation

final class ChallengeWidgetViewModel {
    weak var moduleOutput: ChallengeWidgetOutputProtocol?

    func doRetryContentLoading() {
        moduleOutput?.handleChallengeWidgetRetryContentLoading()
    }

    func doOpenDescriptionLink(_ url: URL) {
        moduleOutput?.handleChallengeWidgetOpenDescriptionLink(url: url)
    }

    func doDeadlineReloadAction() {
        moduleOutput?.handleChallengeWidgetDeadlineReachedReload()
    }

    func doCollectReward() {
        moduleOutput?.handleChallengeWidgetCollectReward()
    }
}
