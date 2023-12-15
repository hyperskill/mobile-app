package org.hyperskill.app.android.topic_search.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentSearchTopicBinding
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.topic_search.ui.TopicSearchResult
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.search.presentation.SearchFeature
import org.hyperskill.app.search.presentation.SearchViewModel
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TopicSearchFragment : Fragment(R.layout.fragment_search_topic) {

    companion object {
        private const val TALKBACK_FOCUS_CHANGE_DELAY_MS: Long = 100

        fun newInstance(): TopicSearchFragment =
            TopicSearchFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val searchViewModel: SearchViewModel by viewModels { requireNotNull(viewModelFactory) }

    private val viewBinding: FragmentSearchTopicBinding by viewBinding(FragmentSearchTopicBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        searchViewModel.handleActions(this, ::handleAction)
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp.graph().buildPlatformSearchComponent().reduxViewModelFactory
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.onNewMessage(
            SearchFeature.Message.ViewedEventMessage
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        viewBinding.topicSearchComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    TopicSearchResult(viewModel = searchViewModel)
                }
            }
        }
    }

    private fun setupToolbar() {
        with(viewBinding) {
            topicSearchToolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            topicSearchClearButton.isVisible = topicSearchEditText.text.isNotEmpty()
            topicSearchClearButton.setOnClickListener {
                topicSearchEditText.text.clear()
            }
            topicSearchEditText.doAfterTextChanged { text ->
                topicSearchClearButton.isVisible = !text.isNullOrEmpty()
                searchViewModel.onNewMessage(
                    SearchFeature.Message.QueryChanged(text.toString())
                )
            }
            topicSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewModel.onNewMessage(
                        SearchFeature.Message.SearchClicked
                    )
                }
                false
            }
            requestFocusAndShowKeyboard(topicSearchEditText)
        }
        setupEditTextRendering()
    }

    private fun requestFocusAndShowKeyboard(
        editText: EditText,
    ) {
        // Without a delay requesting focus on edit text fails when talkback is active.
        editText.postDelayed(
            {
                editText.requestFocus()
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            },
            TALKBACK_FOCUS_CHANGE_DELAY_MS
        )
    }

    private fun setupEditTextRendering() {
        searchViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .map { it.query }
            .onEach { query ->
                viewBinding.topicSearchEditText.setTextIfChanged(query)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleAction(action: SearchFeature.Action.ViewAction) {
        when (action) {
            is SearchFeature.Action.ViewAction.OpenStepScreen -> {
                requireRouter().navigateTo(StepScreen(action.stepRoute))
            }
            is SearchFeature.Action.ViewAction.OpenStepScreenFailed -> {
                Toast.makeText(
                    requireContext(),
                    org.hyperskill.app.R.string.search_open_step_screen_error_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}