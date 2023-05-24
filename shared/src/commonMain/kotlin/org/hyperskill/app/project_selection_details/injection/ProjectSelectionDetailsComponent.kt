package org.hyperskill.app.project_selection_details.injection

import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection_details.presentation.ProjectSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProjectSelectionDetailsComponent {
    fun projectSelectionDetailsFeature(
        projectSelectionDetailsParams: ProjectSelectionDetailsParams
    ): Feature<ViewState, Message, Action>
}