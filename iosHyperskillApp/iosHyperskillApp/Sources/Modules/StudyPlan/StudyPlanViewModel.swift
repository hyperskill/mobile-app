import CombineSchedulers
import shared
import SwiftUI

final class StudyPlanViewModel: FeatureViewModel<
  StudyPlanScreenFeature.ViewState,
  StudyPlanScreenFeatureMessage,
  StudyPlanScreenFeatureActionViewAction
> {
    private var isScreenBecomesActiveFirstTime = true

    var studyPlanWidgetStateKs: StudyPlanWidgetViewStateKs { .init(state.studyPlanWidgetViewState) }
    var gamificationToolbarViewStateKs: GamificationToolbarFeatureViewStateKs { .init(state.toolbarViewState) }
    var usersInterviewWidgetFeatureStateKs: UsersInterviewWidgetFeatureStateKs {
        .init(state.usersInterviewWidgetState)
    }

    override func shouldNotifyStateDidChange(
        oldState: StudyPlanScreenFeature.ViewState,
        newState: StudyPlanScreenFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadStudyPlan() {
        onNewMessage(StudyPlanScreenFeatureMessageInitialize())
    }

    func doRetryContentLoading() {
        onNewMessage(StudyPlanScreenFeatureMessageRetryContentLoading())
    }

    func doScreenBecomesActive() {
        if isScreenBecomesActiveFirstTime {
            isScreenBecomesActiveFirstTime = false
        } else {
            onNewMessage(StudyPlanScreenFeatureMessageScreenBecomesActive())
        }
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

    #warning("Add section id in ALTAPPS-1309")
    func doActivityPresentation(activityId: Int64) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageActivityClicked(
                    activityId: activityId,
                    sectionId: 0
                )
            )
        )
    }

    func doRetryActivitiesLoading(sectionId: Int64) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageRetryActivitiesLoading(sectionId: sectionId)
            )
        )
    }

    func doStreakBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak()
            )
        )
    }

    func doProgressBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedProgress()
            )
        )
    }

    func doProblemsLimitBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageProblemsLimitClicked()
            )
        )
    }

    func doSearchBarButtonItemAction() {
        onNewMessage(
            StudyPlanScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedSearch()
            )
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StudyPlanScreenFeatureMessageViewedEventMessage())
    }
}

// MARK: - StudyPlanViewModel: StageImplementUnsupportedModalViewControllerDelegate -

extension StudyPlanViewModel: StageImplementUnsupportedModalViewControllerDelegate {
    func stageImplementUnsupportedModalViewControllerDidAppear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageStageImplementUnsupportedModalShownEventMessage()
            )
        )
    }

    func stageImplementUnsupportedModalViewControllerDidDisappear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageStageImplementUnsupportedModalHiddenEventMessage()
            )
        )
    }

    func stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        viewController.dismiss(animated: true)

        onNewMessage(
            StudyPlanScreenFeatureMessageStudyPlanWidgetMessage(
                message: StudyPlanWidgetFeatureMessageStageImplementUnsupportedModalGoToHomeClicked()
            )
        )
    }
}

// MARK: - StudyPlanViewModel: UsersInterviewWidgetOutputProtocol -

extension StudyPlanViewModel: UsersInterviewWidgetOutputProtocol {
    func handleUsersInterviewWidgetClicked() {
        onNewMessage(
            StudyPlanScreenFeatureMessageUsersInterviewWidgetMessage(
                message: UsersInterviewWidgetFeatureMessageWidgetClicked()
            )
        )
    }

    func handleUsersInterviewWidgetCloseClicked() {
        onNewMessage(
            StudyPlanScreenFeatureMessageUsersInterviewWidgetMessage(
                message: UsersInterviewWidgetFeatureMessageCloseClicked()
            )
        )
    }

    func handleUsersInterviewWidgetDidAppear() {
        onNewMessage(
            StudyPlanScreenFeatureMessageUsersInterviewWidgetMessage(
                message: UsersInterviewWidgetFeatureMessageViewedEventMessage()
            )
        )
    }
}
