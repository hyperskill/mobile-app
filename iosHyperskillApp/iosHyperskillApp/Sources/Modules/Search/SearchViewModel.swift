import Foundation
import shared

final class SearchViewModel: FeatureViewModel<
  SearchFeature.ViewState,
  SearchFeatureMessage,
  SearchFeatureActionViewAction
> {
    var searchResultsViewStateKs: SearchFeatureSearchResultsViewStateKs { .init(state.searchResultsViewState) }

    override func shouldNotifyStateDidChange(
        oldState: SearchFeature.ViewState,
        newState: SearchFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doQueryChanged(query: String) {
        onNewMessage(SearchFeatureMessageQueryChanged(query: query))
    }

    func doSearch() {
        onNewMessage(SearchFeatureMessageSearchClicked())
    }

    func doRetrySearch() {
        onNewMessage(SearchFeatureMessageRetrySearchClicked())
    }

    func doSearchResultsItemPresentation(id: Int64) {
        onNewMessage(SearchFeatureMessageSearchResultsItemClicked(id: id))
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(SearchFeatureMessageViewedEventMessage())
    }
}

extension SearchFeatureSearchResultsViewStateContent.Item: Identifiable {}
