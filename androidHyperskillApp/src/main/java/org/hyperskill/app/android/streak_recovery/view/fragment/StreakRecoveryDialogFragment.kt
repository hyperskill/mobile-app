package org.hyperskill.app.android.streak_recovery.view.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStreakRecoveryBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme

class StreakRecoveryDialogFragment : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): StreakRecoveryDialogFragment =
            StreakRecoveryDialogFragment()
    }

    private val viewBinding: FragmentStreakRecoveryBinding by viewBinding(FragmentStreakRecoveryBinding::bind)

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
    ): View? {
        return inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_streak_freeze,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            streakRecoveryBuyButton.setOnClickListener {
                (parentFragment as? Callback)?.onConfirmClicked()
            }
            streakRecoveryRejectButton.setOnClickListener {
                (parentFragment as? Callback)?.onRejectClicked()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? Callback)?.onDismiss()
    }

    interface Callback {
        fun onShow()
        fun onDismiss()
        fun onConfirmClicked()
        fun onRejectClicked()
    }
}