package org.hyperskill.app.project_selection.injection

import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProjectSelectionListComponent {
    fun projectSelectionListFeature(trackId: Long): Feature<ViewState, Message, Action>
}