import CombineSchedulers
import shared
import UIKit

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    private var applicationWasInBackground = false
    private var shouldReloadContent = false

    private let problemsLimitViewStateMapper: ProblemsLimitViewStateMapper

    var homeStateKs: HomeFeatureHomeStateKs { .init(state.homeState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }
    var problemsLimitViewStateKs: ProblemsLimitFeatureViewStateKs {
        .init(problemsLimitViewStateMapper.mapState(state: state.problemsLimitState))
    }
    var nextLearningActivityViewStateKs: NextLearningActivityWidgetFeatureViewStateKs {
        .init(
            NextLearningActivityWidgetViewStateMapper.shared.map(
                state: state.nextLearningActivityWidgetState.contentState
            )
        )
    }

    init(
        problemsLimitViewStateMapper: ProblemsLimitViewStateMapper,
        feature: Presentation_reduxFeature,
        mainScheduler: AnySchedulerOf<RunLoop> = .main
    ) {
        self.problemsLimitViewStateMapper = problemsLimitViewStateMapper
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

    func doReloadProblemsLimit() {
        onNewMessage(
            HomeFeatureMessageProblemsLimitMessage(
                message: ProblemsLimitFeatureMessageInitialize(forceUpdate: true)
            )
        )
    }

    func doNextLearningActivityPresentation() {
        onNewMessage(
            HomeFeatureMessageNextLearningActivityWidgetMessage(
                message: NextLearningActivityWidgetFeatureMessageNextLearningActivityClicked()
            )
        )
    }

    func doReloadNextLearningActivity() {
        onNewMessage(
            HomeFeatureMessageNextLearningActivityWidgetMessage(
                message: NextLearningActivityWidgetFeatureMessageRetryContentLoading()
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

// MARK: - StudyPlanViewModel: StageImplementUnsupportedModalViewControllerDelegate -

extension HomeViewModel: StageImplementUnsupportedModalViewControllerDelegate {
    func stageImplementUnsupportedModalViewControllerDidAppear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(
            HomeFeatureMessageStageImplementUnsupportedModalShownEventMessage()
        )
    }

    func stageImplementUnsupportedModalViewControllerDidDisappear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(
            HomeFeatureMessageStageImplementUnsupportedModalHiddenEventMessage()
        )
    }

    func stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        viewController.dismiss(animated: true)

        onNewMessage(
            HomeFeatureMessageStageImplementUnsupportedModalGoToHomeClicked()
        )
    }
}
