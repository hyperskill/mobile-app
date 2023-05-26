package org.hyperskill.app.android.projects_selection.details.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProjectSelectionDetailsFragment : Fragment(), ReduxView<ViewState, ViewAction> {

    companion object {
        fun newInstance(params: ProjectSelectionDetailsParams): ProjectSelectionDetailsFragment =
            ProjectSelectionDetailsFragment().apply {
                this.params = params
            }
    }

    private var params: ProjectSelectionDetailsParams by argument(ProjectSelectionDetailsParams.serializer())

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val projectSelectionDetailsViewModel: ProjectSelectionDetailsViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory) { "ViewModelFactory must be initialized" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val platformProjectSelectListComponent =
            HyperskillApp.graph().buildPlatformProjectSelectionDetailsComponent(params)
        viewModelFactory = platformProjectSelectListComponent.reduxViewModelFactory
    }

    override fun onAction(action: ViewAction) {
        TODO("Not yet implemented")
    }

    override fun render(state: ViewState) {
        TODO("Not yet implemented")
    }
}