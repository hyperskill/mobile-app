package org.hyperskill.app.project_selection.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProjectSelectionListComponentImpl(
    private val projectSelectionListComponent: ProjectSelectionListComponent,
    private val trackId: Long
) : PlatformProjectSelectionListComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                ProjectSelectionListViewModel::class.java to {
                    ProjectSelectionListViewModel(
                        reduxViewContainer = projectSelectionListComponent
                            .projectSelectionListFeature(trackId).wrapWithViewContainer()
                    )
                }
            )
        )
}