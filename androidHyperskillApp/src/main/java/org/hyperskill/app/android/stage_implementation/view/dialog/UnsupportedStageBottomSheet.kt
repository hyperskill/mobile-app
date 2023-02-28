package org.hyperskill.app.android.stage_implementation.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentUnsupportedStageBinding

class UnsupportedStageBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG: String = "UnsupportedStageBottomSheet"

        fun newInstance(): UnsupportedStageBottomSheet =
            UnsupportedStageBottomSheet()
    }

    private val viewBinding: FragmentUnsupportedStageBinding by viewBinding(FragmentUnsupportedStageBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    onNewMessage(/*TODO: Add onShowMessage, see ALTAPPS-635*/)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_unsupported_stage,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.unsupportedStageGoHomeButton.setOnClickListener {
            onNewMessage(/*TODO: Add onHomeButtonCLicked message, see ALTAPPS-635*/)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onNewMessage(/*TODO: Add onHiddenMessage, see ALTAPPS-635*/)
    }

    private fun onNewMessage() {
        TODO("onNewMessage is not implemented yet")
    }
}

fun LayoutInflater.wrapWithTheme(
    context: Context,
    @StyleRes themeRes: Int = R.style.AppTheme
): LayoutInflater =
    cloneInContext(ContextThemeWrapper(context, themeRes))