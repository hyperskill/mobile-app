package org.hyperskill.app.android.track_selection.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackSelectionListBinding
import org.hyperskill.app.android.track_selection.delegate.TrackSelectionListDelegate
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action.ViewAction
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.ViewState
import org.hyperskill.app.track_selection.presentation.TrackSelectionListViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TrackSelectionListFragment : Fragment(R.layout.fragment_track_selection_list), ReduxView<ViewState, ViewAction> {
    companion object {
        fun newInstance() =
            TrackSelectionListFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val trackSelectionListViewModel: TrackSelectionListViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory) { "ViewModelFactory must be initialized" }
    }

    private val viewBinding: FragmentTrackSelectionListBinding by viewBinding(FragmentTrackSelectionListBinding::bind)

    private val imageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    private var trackSelectionListDelegate: TrackSelectionListDelegate? = null

    private var viewStateDelegate: ViewStateDelegate<ViewState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        viewModelFactory = HyperskillApp.graph().buildPlatformTrackSelectionListComponent().reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        trackSelectionListDelegate = TrackSelectionListDelegate(
            context = requireContext(),
            recyclerView = viewBinding.trackSelectionListRecyclerView,
            imageLoader = imageLoader,
            onNewMessage = trackSelectionListViewModel::onNewMessage
        )
        viewStateDelegate = ViewStateDelegate<ViewState>().apply {
            addState<ViewState.Idle>()
            addState<ViewState.Loading>(viewBinding.trackSelectionListRecyclerView)
            addState<ViewState.Content>(viewBinding.trackSelectionListRecyclerView)
            addState<ViewState.Error>(viewBinding.trackSelectionListError.root)
        }
        viewBinding.trackSelectionListToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.trackSelectionListError.tryAgain.setOnClickListener {
            trackSelectionListViewModel.onNewMessage(TrackSelectionListFeature.Message.RetryContentLoading)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        trackSelectionListDelegate = null
        viewStateDelegate = null
    }

    override fun render(state: ViewState) {
        viewStateDelegate?.switchState(state)
        trackSelectionListDelegate?.render(state)
    }

    override fun onAction(action: ViewAction) {
        TODO("Not yet implemented")
    }
}