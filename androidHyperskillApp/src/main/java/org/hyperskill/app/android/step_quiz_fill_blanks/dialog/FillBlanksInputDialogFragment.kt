package org.hyperskill.app.android.step_quiz_fill_blanks.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.requestFocus
import org.hyperskill.app.android.databinding.FragmentFillBlanksInputBinding
import ru.nobird.android.view.base.ui.extension.argument

class FillBlanksInputDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG: String = "FillBlanksInputDialogFragment"

        private const val ARG_INDEX = "INDEX"
        private const val ARG_TEXT = "TEXT"

        fun newInstance(
            index: Int,
            text: String
        ): FillBlanksInputDialogFragment =
            FillBlanksInputDialogFragment().apply {
                this.index = index
                this.text = text
            }
    }

    private var index: Int by argument()
    private var text: String by argument()

    private val fillBlanksInputBinding: FragmentFillBlanksInputBinding by viewBinding(
        FragmentFillBlanksInputBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_fill_blanks_input, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            index = savedInstanceState.getInt(ARG_INDEX)
            text = savedInstanceState.getString(ARG_TEXT) ?: return
        }
        with(fillBlanksInputBinding) {
            fillBlanksInputField.append(text)
            fillBlanksInputField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    super.dismiss()
                }
                false
            }
            fillBlanksInputField.post {
                fillBlanksInputField.requestFocus(requireContext())
            }
        }
    }

    override fun onPause() {
        (parentFragment as? Callback)
            ?.onSyncInputItemWithParent(
                index = index,
                text = fillBlanksInputBinding.fillBlanksInputField.text.toString()
            )
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_INDEX, index)
        outState.putString(ARG_TEXT, text)
    }

    interface Callback {
        fun onSyncInputItemWithParent(index: Int, text: String)
    }
}