package org.hyperskill.app.project_selection.details.injection

import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProjectSelectionDetailsComponent {
    fun projectSelectionDetailsFeature(
        projectSelectionDetailsParams: ProjectSelectionDetailsParams
    ): Feature<ViewState, Message, Action>
}