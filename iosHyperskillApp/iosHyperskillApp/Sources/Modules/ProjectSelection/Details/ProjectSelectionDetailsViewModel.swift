import CombineSchedulers
import Foundation
import shared

final class ProjectSelectionDetailsViewModel: FeatureViewModel<
  ProjectSelectionDetailsFeatureViewState,
  ProjectSelectionDetailsFeatureMessage,
  ProjectSelectionDetailsFeatureActionViewAction
> {
    var stateKs: ProjectSelectionDetailsFeatureViewStateKs { .init(state) }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature, mainScheduler: mainScheduler)
        onNewMessage(ProjectSelectionDetailsFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: ProjectSelectionDetailsFeatureViewState,
        newState: ProjectSelectionDetailsFeatureViewState
    ) -> Bool {
        ProjectSelectionDetailsFeatureViewStateKs(oldState) != ProjectSelectionDetailsFeatureViewStateKs(newState)
    }

    func doRetryLoadProjectSelectionDetails() {
        onNewMessage(ProjectSelectionDetailsFeatureMessageRetryContentLoading())
    }

    func doSelectProjectButtonClicked() {
        onNewMessage(ProjectSelectionDetailsFeatureMessageSelectProjectButtonClicked())
    }

    func doNavigateToStudyPlanAsNewRootScreenPresentation() {
        NotificationCenter.default.post(
            name: .projectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen,
            object: nil
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProjectSelectionDetailsFeatureMessageViewedEventMessage())
    }
}

// MARK: - ProjectSelectionDetailsViewModel (NSNotification.Name) -

extension NSNotification.Name {
    // swiftlint:disable:next identifier_name
    static let projectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen =
      NSNotification.Name("ProjectSelectionDetailsDidRequestNavigateToStudyPlanAsNewRootScreen")
}
