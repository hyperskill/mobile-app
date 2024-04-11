package org.hyperskill.app.android.step_quiz_text.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme

class MarkAsCorrectBottomSheetDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "MarkAsCorrectBottomSheetDialogFragment"

        fun newInstance(): MarkAsCorrectBottomSheetDialogFragment =
            MarkAsCorrectBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_mark_text_step_as_correct,
                container,
                false
            )
}