import Foundation
import shared

final class ProjectSelectionListViewModel: FeatureViewModel<
  ProjectSelectionListFeatureViewState,
  ProjectSelectionListFeatureMessage,
  ProjectSelectionListFeatureActionViewAction
> {
    override func shouldNotifyStateDidChange(
        oldState: ProjectSelectionListFeatureViewState,
        newState: ProjectSelectionListFeatureViewState
    ) -> Bool {
        ProjectSelectionListFeatureViewStateKs(oldState) != ProjectSelectionListFeatureViewStateKs(newState)
    }
}
