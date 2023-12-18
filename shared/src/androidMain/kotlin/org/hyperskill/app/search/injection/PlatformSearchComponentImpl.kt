package org.hyperskill.app.search.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.search.presentation.SearchViewModel

class PlatformSearchComponentImpl(
    private val searchComponent: SearchComponent
) : PlatformSearchComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                SearchViewModel::class.java to {
                    SearchViewModel(
                        searchComponent.searchFeature.wrapWithFlowView()
                    )
                }
            )
        )
}