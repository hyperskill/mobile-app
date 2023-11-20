import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeature.ViewState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var applicationWasInBackground = false
    private var shouldReloadContent = false

    var homeStateKs: HomeFeatureHomeStateKs { .init(state.homeState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

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

    override func shouldNotifyStateDidChange(
        oldState: HomeFeature.ViewState,
        newState: HomeFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadContent(forceUpdate: Bool = false) {
        let forceUpdate = forceUpdate || shouldReloadContent

        onNewMessage(HomeFeatureMessageInitialize(forceUpdate: forceUpdate))

        if shouldReloadContent {
            shouldReloadContent = false
        }
    }

    func doPullToRefresh() {
        onNewMessage(HomeFeatureMessagePullToRefresh())
    }

    func doTopicsRepetitionsPresentation() {
        onNewMessage(HomeFeatureMessageClickedTopicsRepetitionsCard())
    }

    func doStreakBarButtonItemAction() {
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak()
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedGems()
            )
        )
    }

    func doProgressBarButtonItemAction() {
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedProgress()
            )
        )
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
        onNewMessage(
            HomeFeatureMessageClickedProblemOfDayCardReload()
        )
    }

    func handleProblemOfDayOpenStepRequested(stepID: Int) {
        logClickedProblemOfDayCardEvent()

        DispatchQueue.main.async {
            self.onViewAction?(
                HomeFeatureActionViewActionNavigateToStepScreen(
                    stepRoute: StepRouteLearnDaily(stepId: Int64(stepID))
                )
            )
        }
    }
}
