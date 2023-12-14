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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.DialogSearchTopicBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme

class TopicSearchDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "TopicSearchDialogFragment"

        fun newInstance(): TopicSearchDialogFragment =
            TopicSearchDialogFragment()
    }


    private val viewBinding: DialogSearchTopicBinding by viewBinding(DialogSearchTopicBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(R.layout.dialog_search_topic, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            topicSearchToolbar.setNavigationOnClickListener { dismiss() }
            topicSearchClearButton.isVisible = topicSearchEditText.text.isNotEmpty()
            topicSearchEditText.doOnTextChanged { text, _, _, _ ->
                topicSearchClearButton.isVisible = !text.isNullOrEmpty()
                // TODO: notify search request changed
            }
            topicSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // TODO: make search request
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
}