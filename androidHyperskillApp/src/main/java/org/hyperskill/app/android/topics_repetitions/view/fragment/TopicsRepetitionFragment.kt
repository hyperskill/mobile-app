package org.hyperskill.app.android.topics_repetitions.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTopicsRepetitionBinding
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionChartCardDelegate
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionHeaderDelegate
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionListDelegate
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionChartState
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionListState
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionViewModel
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import org.hyperskill.app.topics_repetitions.view.model.TopicsRepetitionsViewData
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TopicsRepetitionFragment :
    Fragment(R.layout.fragment_topics_repetition),
    ReduxView<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            TopicsRepetitionFragment()
    }

    private val viewBinding: FragmentTopicsRepetitionBinding by viewBinding(FragmentTopicsRepetitionBinding::bind)
    private var viewStateDelegate: ViewStateDelegate<TopicsRepetitionsFeature.State>? = null

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val topicsRepetitionViewModel: TopicsRepetitionViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private var viewDataMapper: TopicsRepetitionsViewDataMapper? = null

    private var topicsRepetitionListDelegate: TopicsRepetitionListDelegate? = null
    private var topicsRepetitionHeaderDelegate: TopicsRepetitionHeaderDelegate? = null

    private var viewState: TopicsRepetitionsViewData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val platformTopicsRepetitionsComponent =
            HyperskillApp.graph().buildPlatformTopicsRepetitionsComponent()
        viewModelFactory = platformTopicsRepetitionsComponent.reduxViewModelFactory
        viewDataMapper = platformTopicsRepetitionsComponent.topicsRepetitionsViewDataMapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.topicsRepetitionToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        initViewStateDelegate()
        topicsRepetitionListDelegate = TopicsRepetitionListDelegate(
            viewBinding.topicsList,
            onNewMessage = topicsRepetitionViewModel::onNewMessage
        )
        topicsRepetitionHeaderDelegate = TopicsRepetitionHeaderDelegate(
            viewBinding.topicsRepetitionHeader,
            onNewMessage = topicsRepetitionViewModel::onNewMessage
        )
        with(viewBinding) {
            topicsRepetitionError.tryAgain.setOnClickListener {
                topicsRepetitionViewModel.onNewMessage(
                    TopicsRepetitionsFeature.Message.Initialize(
                        forceUpdate = true
                    )
                )
            }
        }

        topicsRepetitionViewModel.onNewMessage(TopicsRepetitionsFeature.Message.ViewedEventMessage)
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<TopicsRepetitionsFeature.State>().apply {
            addState<TopicsRepetitionsFeature.State.Idle>(viewBinding.topicsRepetitionSkeleton.root)
            addState<TopicsRepetitionsFeature.State.Loading>(viewBinding.topicsRepetitionSkeleton.root)
            addState<TopicsRepetitionsFeature.State.NetworkError>(viewBinding.topicsRepetitionError.root)
            addState<TopicsRepetitionsFeature.State.Content>(
                viewBinding.topicsRepetitionAppBar,
                viewBinding.topicsRepetitionContentNestedScroll
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
        topicsRepetitionListDelegate = null
        topicsRepetitionHeaderDelegate = null
        viewState = null
    }

    override fun onAction(action: TopicsRepetitionsFeature.Action.ViewAction) {
        when (action) {
            is TopicsRepetitionsFeature.Action.ViewAction.NavigateTo.StepScreen ->
                requireRouter().navigateTo(StepScreen(action.stepId))
            TopicsRepetitionsFeature.Action.ViewAction.ShowNetworkError ->
                view?.snackbar(messageRes = R.string.connection_error)
        }
    }

    override fun render(state: TopicsRepetitionsFeature.State) {
        viewStateDelegate?.switchState(state)
        val mapper = viewDataMapper
        if (mapper != null && state is TopicsRepetitionsFeature.State.Content) {
            viewLifecycleOwner.lifecycleScope.launch {
                val viewState = withContext(Dispatchers.Default) {
                    mapper.mapStateToViewData(state)
                }
                topicsRepetitionHeaderDelegate?.render(
                    context = requireContext(),
                    previousState = this@TopicsRepetitionFragment.viewState?.repetitionsStatus,
                    state = viewState.repetitionsStatus
                )
                topicsRepetitionListDelegate?.render(
                    previousState = this@TopicsRepetitionFragment.viewState?.let {
                        TopicsRepetitionListState(
                            repeatBlockTitle = it.repeatBlockTitle,
                            trackTopicsTitle = it.trackTopicsTitle,
                            topicsToRepeat = it.topicsToRepeatFromCurrentTrack,
                            topicsToRepeatWillLoadedCount = it.topicsToRepeatWillLoadedCount,
                            showMoreButtonState = it.showMoreButtonState
                        )
                    },
                    state = TopicsRepetitionListState(
                        repeatBlockTitle = viewState.repeatBlockTitle,
                        trackTopicsTitle = viewState.trackTopicsTitle,
                        topicsToRepeat = viewState.topicsToRepeatFromCurrentTrack,
                        topicsToRepeatWillLoadedCount = viewState.topicsToRepeatWillLoadedCount,
                        showMoreButtonState = viewState.showMoreButtonState
                    )
                )
                TopicsRepetitionChartCardDelegate.render(
                    binding = viewBinding.topicsRepetitionHeader.topicsRepetitionChart,
                    previousState = this@TopicsRepetitionFragment.viewState?.let {
                        TopicsRepetitionChartState(it.chartData, it.chartDescription)
                    },
                    state = TopicsRepetitionChartState(viewState.chartData, viewState.chartDescription)
                )
                this@TopicsRepetitionFragment.viewState = viewState
            }
        }
    }
}