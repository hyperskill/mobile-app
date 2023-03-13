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

    @Suppress("unused")
    companion object {
        const val TAG: String = "UnsupportedStageBottomSheet"

        /**
         * Caller should implement [Callback] interface.
         */
        fun Callback.newInstance(): UnsupportedStageBottomSheet =
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
                    (parentFragment as? Callback)?.onShow()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_unsupported_stage,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.unsupportedStageGoHomeButton.setOnClickListener {
            (parentFragment as? Callback)?.onHomeClick()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? Callback)?.onDismiss()
    }

    interface Callback {
        fun onShow()

        fun onDismiss()

        fun onHomeClick()
    }
}

fun LayoutInflater.wrapWithTheme(
    context: Context,
    @StyleRes themeRes: Int = R.style.AppTheme
): LayoutInflater =
    cloneInContext(ContextThemeWrapper(context, themeRes))