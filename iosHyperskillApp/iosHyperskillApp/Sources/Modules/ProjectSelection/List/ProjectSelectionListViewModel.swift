import CombineSchedulers
import Foundation
import shared

final class ProjectSelectionListViewModel: FeatureViewModel<
  ProjectSelectionListFeatureViewState,
  ProjectSelectionListFeatureMessage,
  ProjectSelectionListFeatureActionViewAction
> {
    var viewStateKs: ProjectSelectionListFeatureViewStateKs { .init(state) }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature, mainScheduler: mainScheduler)
        onNewMessage(ProjectSelectionListFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: ProjectSelectionListFeatureViewState,
        newState: ProjectSelectionListFeatureViewState
    ) -> Bool {
        ProjectSelectionListFeatureViewStateKs(oldState) != ProjectSelectionListFeatureViewStateKs(newState)
    }

    func doRetryLoadProjectSelectionList() {
        onNewMessage(ProjectSelectionListFeatureMessageRetryContentLoading())
    }

    func doMainProjectAction(projectID: Int64) {
        onNewMessage(ProjectSelectionListFeatureMessageProjectClicked(projectId: projectID))
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProjectSelectionListFeatureMessageViewedEventMessage())
    }
}
