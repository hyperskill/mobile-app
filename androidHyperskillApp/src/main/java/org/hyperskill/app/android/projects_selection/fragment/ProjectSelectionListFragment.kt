package org.hyperskill.app.android.projects_selection.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentProjectSelectionListBinding
import org.hyperskill.app.android.projects_selection.delegate.ProjectSelectionListDelegate
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Action.ViewAction
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.Message
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature.ViewState
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListViewModel
import org.hyperskill.app.projects.domain.model.Project
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProjectSelectionListFragment :
    Fragment(R.layout.fragment_project_selection_list),
    ReduxView<ViewState, ViewAction> {
    companion object {
        fun newInstance(trackId: Long): ProjectSelectionListFragment =
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

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    private var projectSelectionListDelegate: ProjectSelectionListDelegate? = null

    private var viewStateDelegate: ViewStateDelegate<ViewState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val platformProjectSelectListComponent =
            HyperskillApp.graph().buildPlatformProjectSelectionListComponent(trackId)
        viewModelFactory = platformProjectSelectListComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewStateDelegate = ViewStateDelegate<ViewState>().apply {
            addState<ViewState.Idle>()
            addState<ViewState.Loading>(viewBinding.projectSelectionListSkeleton.root)
            addState<ViewState.Error>(viewBinding.projectSelectionListError.root)
            addState<ViewState.Content>(viewBinding.projectSelectionListRecyclerView)
        }
        projectSelectionListDelegate = ProjectSelectionListDelegate(
            context = requireContext(),
            recyclerView = viewBinding.projectSelectionListRecyclerView,
            imageLoader = imageLoader,
            onNewMessage = projectSelectionListViewModel::onNewMessage
        )
        viewBinding.projectSelectionListToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.projectSelectionListError.tryAgain.setOnClickListener {
            projectSelectionListViewModel.onNewMessage(Message.RetryContentLoading)
        }
    }

    override fun render(state: ViewState) {
        viewStateDelegate?.switchState(state)
        if (state is ViewState.Content) {
            projectSelectionListDelegate?.render(state)
            if (state.isProjectSelectionLoadingShowed) {
                LoadingProgressDialogFragment.newInstance()
                    .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
            } else {
                childFragmentManager
                    .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
        }
    }

    override fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NavigateTo.StudyPlan ->
                requireRouter().backTo(StudyPlanScreen)
            is ViewAction.ShowProjectSelectionConfirmationModal -> {
                buildProjectSelectionConfirmationModal(action.project).show()
                projectSelectionListViewModel.onNewMessage(
                    Message.ProjectSelectionConfirmationModalShown
                )
            }
            ViewAction.ShowProjectSelectionStatus.Error ->
                view?.snackbar(org.hyperskill.app.R.string.connection_error)
            ViewAction.ShowProjectSelectionStatus.Success ->
                view?.snackbar(org.hyperskill.app.R.string.projects_list_selection_succeed)
        }
    }

    private fun buildProjectSelectionConfirmationModal(project: Project): MaterialAlertDialogBuilder =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(
                getString(
                    org.hyperskill.app.R.string.projects_list_select_confirmation_title,
                    project.title
                )
            )
            .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                projectSelectionListViewModel.onNewMessage(
                    Message.ProjectSelectionConfirmationResult(
                        projectId = project.id,
                        isConfirmed = true
                    )
                )
            }
            .setNegativeButton(org.hyperskill.app.R.string.no) { _, _ ->
                projectSelectionListViewModel.onNewMessage(
                    Message.ProjectSelectionConfirmationResult(
                        projectId = project.id,
                        isConfirmed = false
                    )
                )
            }
            .setOnDismissListener {
                projectSelectionListViewModel.onNewMessage(
                    Message.ProjectSelectionConfirmationResult(
                        projectId = project.id,
                        isConfirmed = false
                    )
                )
                projectSelectionListViewModel.onNewMessage(
                    Message.ProjectSelectionConfirmationModalHidden
                )
            }
}