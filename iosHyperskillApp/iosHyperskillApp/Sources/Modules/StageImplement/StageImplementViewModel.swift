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
    }

    override func shouldNotifyStateDidChange(
        oldState: StageImplementFeatureViewState,
        newState: StageImplementFeatureViewState
    ) -> Bool {
        StageImplementFeatureViewStateKs(oldState) != StageImplementFeatureViewStateKs(newState)
    }

    func doLoadStageImplement(forceUpdate: Bool = false) {
        onNewMessage(
            StageImplementFeatureMessageInitialize(projectId: projectID, stageId: stageID, forceUpdate: forceUpdate)
        )
    }

    func doDeprecatedButtonClicked() {
        onNewMessage(StageImplementFeatureMessageProjectDeprecatedButtonClicked())
    }

    func logViewedEvent() {
        onNewMessage(StageImplementFeatureMessageViewedEventMessage())
    }
}

// MARK: - StageImplementViewModel: StageImplementUnsupportedModalViewControllerDelegate -

extension StageImplementViewModel: StageImplementUnsupportedModalViewControllerDelegate {
    func stageImplementUnsupportedModalViewControllerViewControllerDidAppear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageUnsupportedModalShownEventMessage())
    }

    func stageImplementUnsupportedModalViewControllerDidDisappear(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageUnsupportedModalHiddenEventMessage())
    }

    func stageImplementUnsupportedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: StageImplementUnsupportedModalViewController
    ) {
        onNewMessage(StageImplementFeatureMessageUnsupportedModalGoToHomeScreenClicked())
    }
}
