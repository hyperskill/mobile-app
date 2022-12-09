package org.hyperskill.app.android.placeholder_new_user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.android.databinding.ItemNewUserTrackBinding
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import org.hyperskill.app.placeholder_new_user.view.mapper.PlaceholderNewUserViewDataMapper
import org.hyperskill.app.placeholder_new_user.view.model.PlaceholderNewUserTrack
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class PlaceholderNewUserFragment :
    Fragment(R.layout.fragment_placeholder_new_user_screen),
    ReduxView<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private val viewBinding: FragmentPlaceholderNewUserScreenBinding by viewBinding(
        FragmentPlaceholderNewUserScreenBinding::bind
    )

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val placeholderNewUserViewModel: PlaceholderNewUserViewModel by reduxViewModel(this) { viewModelFactory }

    private var viewDataMapper: PlaceholderNewUserViewDataMapper? = null

    private val svgImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        ImageLoader.Builder(requireContext())
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    private val trackAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<PlaceholderNewUserTrack>().apply {
            addDelegate(trackAdapterDelegate(svgImageLoader))
        }
    }

    private var viewStateDelegate: ViewStateDelegate<PlaceholderNewUserFeature.State>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val placeholderNewUserComponent = HyperskillApp.graph().buildPlaceholderNewUserComponent()
        val platformPlaceholderNewUserComponent =
            HyperskillApp.graph().buildPlatformPlaceholderNewUserComponent(placeholderNewUserComponent)
        viewModelFactory = platformPlaceholderNewUserComponent.reduxViewModelFactory
        viewDataMapper = placeholderNewUserComponent.placeHolderNewUserViewDataMapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewStateDelegate()
        setupRecyclerView()
        viewBinding.placeholderError.tryAgain.setOnClickListener {
            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.Initialize(forceUpdate = true))
        }
        placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ViewedEventMessage)
    }

    private fun setupViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<PlaceholderNewUserFeature.State>().apply {
            addState<PlaceholderNewUserFeature.State.Idle>()
            addState<PlaceholderNewUserFeature.State.Loading>(viewBinding.placeholderProgress)
            addState<PlaceholderNewUserFeature.State.NetworkError>(viewBinding.placeholderError.root)
            addState<PlaceholderNewUserFeature.State.Content>(viewBinding.placeholderContentConstraintLayout)
        }
    }

    private fun setupRecyclerView() {
        with(viewBinding.placeholderRecyclerView) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = trackAdapter
            addItemDecoration(
                VerticalMarginItemDecoration(
                    verticalMargin = requireContext().resources.getDimensionPixelSize(R.dimen.track_items_vertical_margin)
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onAction(action: PlaceholderNewUserFeature.Action.ViewAction) {
        /*when (action) {
            PlaceholderNewUserFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().newRootScreen(AuthScreen)
            is PlaceholderNewUserFeature.Action.ViewAction.OpenUrl ->
                requireContext().launchUrl(action.url)
            PlaceholderNewUserFeature.Action.ViewAction.ShowGetMagicLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
        }*/
    }

    override fun render(state: PlaceholderNewUserFeature.State) {
        viewStateDelegate?.switchState(state)
        if (state is PlaceholderNewUserFeature.State.Content) {
            viewDataMapper?.let { mapper ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val tracks = withContext(Dispatchers.Default) {
                        mapper.mapStateToTracks(state)
                    }
                    trackAdapter.items = tracks
                }
            }
        }
    }

    private fun trackAdapterDelegate(imageLoader: ImageLoader) =
        adapterDelegate<PlaceholderNewUserTrack, PlaceholderNewUserTrack>(
            R.layout.item_new_user_track
        ) {
            itemView.setOnClickListener {
                item?.let { track ->
                    placeholderNewUserViewModel.onNewMessage(
                        PlaceholderNewUserFeature.Message.TrackTappedEventMessage(track.id)
                    )
                }
            }
            val viewBinding = ItemNewUserTrackBinding.bind(itemView)
            onBind { track ->
                with(viewBinding) {
                    newUserTrackNameTextView.setTextIfChanged(track.title)
                    newUserTrackDurationTextView.setTextIfChanged(track.timeToComplete)
                    newUserTrackRatingTextView.setTextIfChanged(track.rating)
                    newUserTrackIconImageView.load(track.imageSource, imageLoader)
                }
            }
        }
}