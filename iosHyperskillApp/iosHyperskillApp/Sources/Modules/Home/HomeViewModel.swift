import CombineSchedulers
import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var applicationWasInBackground = false
    private var shouldReloadContent = false

    var stateKs: HomeFeatureStateKs { .init(state) }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature, mainScheduler: mainScheduler)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: UIApplication.shared
        )
    }

    override func shouldNotifyStateDidChange(oldState: HomeFeatureState, newState: HomeFeatureState) -> Bool {
        HomeFeatureStateKs(oldState) != HomeFeatureStateKs(newState)
    }

    func doLoadContent(forceUpdate: Bool = false) {
        onNewMessage(HomeFeatureMessageInitialize(forceUpdate: forceUpdate || shouldReloadContent))

        if shouldReloadContent {
            shouldReloadContent = false
        }
    }

    func doPullToRefresh() {
        onNewMessage(HomeFeatureMessagePullToRefresh())
    }

    func doContinueLearningOnWebPresentation() {
        onNewMessage(HomeFeatureMessageClickedContinueLearningOnWeb())
    }

    func handleTopicsRepetitionsRequested() {
        mainScheduler.schedule {
            self.onViewAction?(HomeFeatureActionViewActionNavigateToTopicsRepetitionsScreen())
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(HomeFeatureMessageViewedEventMessage())
    }

    private func logClickedProblemOfDayCardEvent() {
        onNewMessage(HomeFeatureMessageClickedProblemOfDayCardEventMessage())
    }

    // MARK: Private API

    @objc
    private func handleApplicationDidBecomeActive() {
        guard applicationWasInBackground else {
            return
        }

        applicationWasInBackground = false
        shouldReloadContent = true
    }

    @objc
    private func handleApplicationDidEnterBackground() {
        applicationWasInBackground = true
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        doLoadContent(forceUpdate: true)
    }

    func handleProblemOfDayOpenStepRequested(stepID: Int) {
        logClickedProblemOfDayCardEvent()

        DispatchQueue.main.async {
            self.onViewAction?(HomeFeatureActionViewActionNavigateToStepScreen(stepId: Int64(stepID)))
        }
    }
}
