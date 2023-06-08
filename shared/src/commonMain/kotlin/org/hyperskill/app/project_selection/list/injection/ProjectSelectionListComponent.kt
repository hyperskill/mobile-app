package org.hyperskill.app.project_selection.list.injection

import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProjectSelectionListComponent {
    fun projectSelectionListFeature(params: ProjectSelectionListParams): Feature<ViewState, Message, Action>
}