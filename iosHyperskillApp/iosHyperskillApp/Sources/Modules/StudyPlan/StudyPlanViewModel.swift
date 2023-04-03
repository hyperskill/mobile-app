import shared
import SwiftUI

final class StudyPlanViewModel: FeatureViewModel<
  StudyPlanScreenViewState,
  StudyPlanScreenFeatureMessage,
  StudyPlanScreenFeatureActionViewAction
> {
    var studyPlanWidgetStateKs: StudyPlanWidgetViewStateKs { .init(state.studyPlanWidgetViewState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }

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
}
