import Foundation
import shared

final class StageImplementViewModel: FeatureViewModel<
  StageImplementFeatureViewState,
  StageImplementFeatureMessage,
  StageImplementFeatureActionViewAction
> {
    private let projectID: Int64
    private let stageID: Int64

    var stateKs: StageImplementFeatureViewStateKs { .init(state) }

    init(
        projectID: Int64,
        stageID: Int64,
        feature: Presentation_reduxFeature
    ) {
        self.projectID = projectID
        self.stageID = stageID

        super.init(feature: feature)

        onNewMessage(
            StageImplementFeatureMessageInitialize(projectId: projectID, stageId: stageID, forceUpdate: false)
        )
    }

    override func shouldNotifyStateDidChange(
        oldState: StageImplementFeatureViewState,
        newState: StageImplementFeatureViewState
    ) -> Bool {
        StageImplementFeatureViewStateKs(oldState) != StageImplementFeatureViewStateKs(newState)
    }

    func doRetryLoadStageImplement() {
        onNewMessage(
            StageImplementFeatureMessageInitialize(projectId: projectID, stageId: stageID, forceUpdate: true)
        )
    }

    func logViewedEvent() {
        onNewMessage(StageImplementFeatureMessageViewedEventMessage())
    }
}

// MARK: - StageImplementViewModel: StageImplementStageCompletedModalViewControllerDelegate -

extension StageImplementViewModel: StageImplementStageCompletedModalViewControllerDelegate {
    func stageImplementStageCompletedModalViewControllerDidAppear(
        _ viewController: StageImplementStageCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageStageCompletedModalShownEventMessage())
    }

    func stageImplementStageCompletedModalViewControllerDidDisappear(
        _ viewController: StageImplementStageCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageStageCompletedModalHiddenEventMessage())
    }

    func stageImplementStageCompletedModalViewControllerDidTapCallToActionButton(
        _ viewController: StageImplementStageCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageStageCompletedModalGoToStudyPlanClicked())
    }
}

// MARK: - StageImplementViewModel: StageImplementProjectCompletedModalViewControllerDelegate -

extension StageImplementViewModel: StageImplementProjectCompletedModalViewControllerDelegate {
    func stageImplementProjectCompletedModalViewControllerDidAppear(
        _ viewController: StageImplementProjectCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageProjectCompletedModalShownEventMessage())
    }

    func stageImplementProjectCompletedModalViewControllerDidDisappear(
        _ viewController: StageImplementProjectCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageProjectCompletedModalHiddenEventMessage())
    }

    func stageImplementProjectCompletedModalViewControllerDidTapCallToActionButton(
        _ viewController: StageImplementProjectCompletedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageProjectCompletedModalGoToStudyPlanClicked())
    }
}
