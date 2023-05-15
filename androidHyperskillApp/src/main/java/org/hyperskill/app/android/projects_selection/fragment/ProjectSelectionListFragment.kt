package org.hyperskill.app.android.projects_selection.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProjectSelectionListBinding
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action.ViewAction
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListViewModel
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProjectSelectionListFragment :
    Fragment(R.layout.fragment_project_selection_list),
    ReduxView<ViewState, ViewAction> {
    companion object {
        fun newInstance(trackId: Long) =
            ProjectSelectionListFragment().apply {
                this.trackId = trackId
            }
    }

    private var trackId: Long by argument()

    private val viewBinding: FragmentProjectSelectionListBinding by viewBinding(
        FragmentProjectSelectionListBinding::bind
    )

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val projectSelectionListViewModel: ProjectSelectionListViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val platformProjectSelectListComponent =
            HyperskillApp.graph().buildPlatformProjectSelectionListComponent(trackId)
        viewModelFactory = platformProjectSelectListComponent.reduxViewModelFactory
    }

    override fun render(state: ViewState) {
        TODO("Not yet implemented")
    }

    override fun onAction(action: ViewAction) {
        TODO("Not yet implemented")
    }
}