import shared
import SwiftUI

@available(iOS 15.0, *)
struct SearchView: View {
    @StateObject var viewModel: SearchViewModel

    var stackRouter: StackRouterProtocol

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
        }
        .searchable(
            text: Binding<String>.init(
                get: { viewModel.state.query },
                set: viewModel.doQueryChanged(query:)
            )
        )
        .onSubmit(of: .search, viewModel.doSearch)
        .introspectViewController { viewController in
            guard viewModel.shouldBecomeFirstResponder() else {
                return
            }

            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                viewController.navigationItem.searchController?.searchBar.becomeFirstResponder()
            }
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.searchResultsViewStateKs {
        case .idle, .loading:
            if viewModel.state.query.isEmpty {
                SearchPlaceholderSuggestionsView()
            } else {
                SearchPlaceholderLoadingView()
            }
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    titleText: Strings.Search.placeholderErrorDescription,
                    buttonText: Strings.StepQuiz.retryButton,
                    backgroundColor: .clear,
                    action: viewModel.doRetrySearch
                )
            )
        case .empty:
            SearchPlaceholderEmptyView()
        case .content(let data):
            List(data.searchResults) { searchResult in
                Button(
                    action: {
                        viewModel.doSearchResultsItemPresentation(id: searchResult.id.int64Value)
                    },
                    label: {
                        HStack {
                            Text(searchResult.title)
                            Spacer()
                            NavigationLink.empty
                        }
                    }
                )
                .accentColor(.primaryText)
            }
            .listStyle(.insetGrouped)
        }
    }
}

// MARK: - SearchView (ViewAction) -

@available(iOS 15.0, *)
private extension SearchView {
    func handleViewAction(
        _ viewAction: SearchFeatureActionViewAction
    ) {
        switch SearchFeatureActionViewActionKs(viewAction) {
        case .openStepScreen(let openStepScreenViewAction):
            let assembly = StepAssembly(stepRoute: openStepScreenViewAction.stepRoute)
            stackRouter.pushViewController(assembly.makeModule())
        case .openStepScreenFailed(let openStepScreenFailedViewAction):
            ProgressHUD.showError(status: openStepScreenFailedViewAction.message)
        }
    }
}

// MARK: - SearchView (Preview) -

@available(iOS 17, *)
#Preview {
    SearchAssembly().makeModule()
}
