import Foundation
import shared

final class StepQuizHintsViewModel: FeatureViewModel<
  StepQuizHintsFeatureState,
  StepQuizHintsFeatureMessage,
  StepQuizHintsFeatureActionViewAction
> {
    let stepID: Int64

    init(stepID: Int64, feature: Presentation_reduxFeature) {
        self.stepID = stepID
        super.init(feature: feature)
    }

    func loadHintsIDs() {
        onNewMessage(StepQuizHintsFeatureMessageInitWithStepId(stepId: stepID))
    }

    func onHintReactionButtonTap(reaction: ReactionType) {
        onNewMessage(StepQuizHintsFeatureMessageReactionButtonClicked(reaction: reaction))
    }

    func onHintReportModalAppear() {
        onNewMessage(StepQuizHintsFeatureMessageReportHintReportShownEventMessage())
    }

    func onHintReportButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageReportHintReportClickedEventMessage())
    }

    func onHintReportConfirmationButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageHintReported())
    }

    func onHintReportCancelingButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageReportHintNoticeHiddenEventMessage())
    }

    func onLoadHintButtonTap() {
        onNewMessage(StepQuizHintsFeatureMessageLoadHintButtonClicked())
    }

    func onSeeHintButtonTap() {
        onLoadHintButtonTap()
        onNewMessage(StepQuizHintsFeatureMessageReportSeeHintClickedEventMessage())
    }
}
