package org.hyperskill.app.project_selection_details.view

import org.hyperskill.app.core.view.mapper.NumbersFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature

internal class ProjectSelectionDetailsViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val numbersFormatter: NumbersFormatter
) {
    fun map(state: ProjectSelectionDetailsFeature.State): ProjectSelectionDetailsFeature.ViewState =
        when (state.contentState) {
            ProjectSelectionDetailsFeature.ContentState.Idle -> TODO()
            ProjectSelectionDetailsFeature.ContentState.Loading -> TODO()
            ProjectSelectionDetailsFeature.ContentState.Error -> TODO()
            is ProjectSelectionDetailsFeature.ContentState.Content -> TODO()
        }
}