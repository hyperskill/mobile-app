import Foundation
import shared

final class StepQuizHintsViewModel: FeatureViewModel<
  StepQuizHintsFeatureViewState,
  StepQuizHintsFeatureMessage,
  StepQuizHintsFeatureActionViewAction
> {
    let stepID: Int64

    var stateKs: StepQuizHintsFeatureViewStateKs { .init(state) }

    init(stepID: Int64, feature: Presentation_reduxFeature) {
        self.stepID = stepID
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(
        oldState: StepQuizHintsFeatureViewState,
        newState: StepQuizHintsFeatureViewState
    ) -> Bool {
        StepQuizHintsFeatureViewStateKs(oldState) != StepQuizHintsFeatureViewStateKs(newState)
    }

    func loadHintsIDs() {
        onNewMessage(StepQuizHintsFeatureMessageInitWithStepId(stepId: stepID))
    }

    func onHintReactionButtonTap(reaction: ReactionType) {
        onNewMessage(StepQuizHintsFeatureMessageReactionButtonClicked(reaction: reaction))
    }

    func onHintReportConfirmationButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageReportHint())
    }

    func onLoadHintButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageLoadHintButtonClicked())
    }

    // MARK: Analytic

    func logHintNoticeShownEvent() {
        onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeShownEventMessage())
    }

    func logClickedReportEvent() {
        onNewMessage(StepQuizHintsFeatureMessageClickedReportEventMessage())
    }

    func logHintNoticeHiddenEvent(isReported: Bool) {
        onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeHiddenEventMessage(isReported: isReported))
    }
}
