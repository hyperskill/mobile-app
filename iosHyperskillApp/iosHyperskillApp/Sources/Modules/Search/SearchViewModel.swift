import Foundation
import shared

final class SearchViewModel: FeatureViewModel<
  SearchFeature.ViewState,
  SearchFeatureMessage,
  SearchFeatureActionViewAction
> {
    var searchResultsViewStateKs: SearchFeatureSearchResultsViewStateKs { .init(state.searchResultsViewState) }

    private var isFirstTimeBecomeFirstResponder = true

    override func shouldNotifyStateDidChange(
        oldState: SearchFeature.ViewState,
        newState: SearchFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func shouldBecomeFirstResponder() -> Bool {
        if isFirstTimeBecomeFirstResponder {
            isFirstTimeBecomeFirstResponder = false
            return true
        } else {
            return false
        }
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

extension shared.SearchFeatureSearchResultsViewStateContent.Item: @retroactive Swift.Identifiable {}
