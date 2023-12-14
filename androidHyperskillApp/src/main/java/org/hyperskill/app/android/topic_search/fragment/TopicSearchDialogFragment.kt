package org.hyperskill.app.android.topic_search.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.DialogSearchTopicBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.search.presentation.SearchFeature
import org.hyperskill.app.search.presentation.SearchViewModel
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TopicSearchDialogFragment : DialogFragment(), ReduxView<SearchFeature.ViewState, SearchFeature.Action.ViewAction> {

    companion object {
        const val TAG = "TopicSearchDialogFragment"

        fun newInstance(): TopicSearchDialogFragment =
            TopicSearchDialogFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val searchViewModel: SearchViewModel by reduxViewModel(this) { requireNotNull(viewModelFactory) }

    private val viewBinding: DialogSearchTopicBinding by viewBinding(DialogSearchTopicBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp.graph().buildPlatformSearchComponent().reduxViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(R.layout.dialog_search_topic, container, false)

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            window.setWindowAnimations(R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        }
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.onNewMessage(
            SearchFeature.Message.ViewedEventMessage
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            topicSearchToolbar.setNavigationOnClickListener { dismiss() }
            topicSearchClearButton.isVisible = topicSearchEditText.text.isNotEmpty()
            topicSearchEditText.doAfterTextChanged { text ->
                topicSearchClearButton.isVisible = !text.isNullOrEmpty()
                searchViewModel.onNewMessage(
                    SearchFeature.Message.QueryChanged(text.toString())
                )
            }
            topicSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SearchFeature.Message.SearchClicked
                }
                false
            }
            topicSearchEditText.post {
                topicSearchEditText.requestFocus()
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(topicSearchEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    override fun render(state: SearchFeature.ViewState) {
        viewBinding.topicSearchEditText.setTextIfChanged(state.query)
        when (state.searchResultsViewState) {
            SearchFeature.SearchResultsViewState.Empty -> TODO()
            SearchFeature.SearchResultsViewState.Idle -> TODO()
            SearchFeature.SearchResultsViewState.Loading -> TODO()
            SearchFeature.SearchResultsViewState.Error -> TODO()
            is SearchFeature.SearchResultsViewState.Content -> TODO()
        }
    }

    override fun onAction(action: SearchFeature.Action.ViewAction) {
        when (action) {
            is SearchFeature.Action.ViewAction.OpenStepScreen -> TODO()
            is SearchFeature.Action.ViewAction.OpenStepScreenFailed -> TODO()
        }
    }
}