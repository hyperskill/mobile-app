import Foundation
import shared

final class ProjectSelectionListViewModel: FeatureViewModel<
  ProjectSelectionListFeatureViewState,
  ProjectSelectionListFeatureMessage,
  ProjectSelectionListFeatureActionViewAction
> {
    var viewStateKs: ProjectSelectionListFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: ProjectSelectionListFeatureViewState,
        newState: ProjectSelectionListFeatureViewState
    ) -> Bool {
        ProjectSelectionListFeatureViewStateKs(oldState) != ProjectSelectionListFeatureViewStateKs(newState)
    }

    func doLoadProjectSelectionList() {
        onNewMessage(ProjectSelectionListFeatureMessageInitialize())
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
