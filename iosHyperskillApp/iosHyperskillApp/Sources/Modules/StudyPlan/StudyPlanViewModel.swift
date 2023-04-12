import CombineSchedulers
import shared
import SwiftUI

final class StudyPlanViewModel: FeatureViewModel<
  StudyPlanScreenViewState,
  StudyPlanScreenFeatureMessage,
  StudyPlanScreenFeatureActionViewAction
> {
    private var applicationWasInBackground = false
    private var shouldReloadContent = false

    var studyPlanWidgetStateKs: StudyPlanWidgetViewStateKs { .init(state.studyPlanWidgetViewState) }
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

    override func shouldNotifyStateDidChange(
        oldState: StudyPlanScreenViewState,
        newState: StudyPlanScreenViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadStudyPlan() {
        onNewMessage(StudyPlanScreenFeatureMessageInitialize())
    }

    func doRetryContentLoading() {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageRetryContentLoading()
            )
        )
    }

    func doScreenBecomesActive() {
        onNewMessage(
            StudyPlanScreenFeatureMessageScreenBecomesActive()
        )
    }

    func doPullToRefresh() {
        onNewMessage(StudyPlanScreenFeatureMessagePullToRefresh())
    }

    func doSectionToggle(sectionId: Int64) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageSectionClicked(sectionId: sectionId)
            )
        )
    }

    func doActivityPresentation(activityId: Int64) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageActivityClicked(activityId: activityId)
            )
        )
    }

    func doStreakBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak(screen: GamificationToolbarScreen.track)
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedGems(screen: GamificationToolbarScreen.track)
            )
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StudyPlanScreenFeatureMessageViewedEventMessage())
    }

    // MARK: Private API

    @objc
    private func handleApplicationDidBecomeActive() {
        guard applicationWasInBackground else {
            return
        }

        doScreenBecomesActive()

        applicationWasInBackground = false
    }

    @objc
    private func handleApplicationDidEnterBackground() {
        applicationWasInBackground = true
    }
}
