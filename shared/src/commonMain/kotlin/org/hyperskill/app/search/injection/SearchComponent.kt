package org.hyperskill.app.search.injection

import org.hyperskill.app.search.presentation.SearchFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface SearchComponent {
    val searchFeature: Feature<SearchFeature.ViewState, SearchFeature.Message, SearchFeature.Action>
}