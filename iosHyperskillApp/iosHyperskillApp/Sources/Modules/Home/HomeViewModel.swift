import CombineSchedulers
import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var applicationWasInBackground = false
    private var shouldReloadContent = false

    var homeStateKs: HomeFeatureHomeStateKs { .init(state.homeState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }

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
        !oldState.isEqual(newState)
    }

    func doLoadContent(forceUpdate: Bool = false) {
        let forceUpdate = forceUpdate || shouldReloadContent

        onNewMessage(HomeFeatureMessageInitialize(forceUpdate: forceUpdate))
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageInitialize(
                    screen: GamificationToolbarScreen.home,
                    forceUpdate: forceUpdate
                )
            )
        )

        if shouldReloadContent {
            shouldReloadContent = false
        }
    }

    func doPullToRefresh() {
        onNewMessage(HomeFeatureMessagePullToRefresh())
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessagePullToRefresh(screen: GamificationToolbarScreen.home)
            )
        )
    }

    func doContinueLearningOnWebPresentation() {
        onNewMessage(HomeFeatureMessageClickedContinueLearningOnWeb())
    }

    func doTopicsRepetitionsPresentation() {
        onNewMessage(HomeFeatureMessageClickedTopicsRepetitionsCard())
    }

    func doStreakBarButtonItemAction() {
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak(screen: GamificationToolbarScreen.home)
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            HomeFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedGems(screen: GamificationToolbarScreen.home)
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
        doLoadContent(forceUpdate: true)
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
