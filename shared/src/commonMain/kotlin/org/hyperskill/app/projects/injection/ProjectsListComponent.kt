package org.hyperskill.app.projects.injection

import org.hyperskill.app.projects.presentation.ProjectsListFeature.Action
import org.hyperskill.app.projects.presentation.ProjectsListFeature.Message
import org.hyperskill.app.projects.presentation.ProjectsListFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProjectsListComponent {
    fun projectsListFeature(trackId: Long): Feature<ViewState, Message, Action>
}