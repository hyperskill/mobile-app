package org.hyperskill.app.android.streak_recovery.view.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentStreakRecoveryBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Action.ViewAction.ShowRecoveryStreakModal

class StreakRecoveryDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG: String = "StreakRecoveryDialogFragment"
        fun newInstance(params: ShowRecoveryStreakModal): StreakRecoveryDialogFragment =
            StreakRecoveryDialogFragment().apply {
                this.params = params
            }
    }

    private var params: ShowRecoveryStreakModal by argument(ShowRecoveryStreakModal.serializer())

    private val viewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)

    private val viewBinding: FragmentStreakRecoveryBinding by viewBinding(FragmentStreakRecoveryBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    viewModel.onNewMessage(
                        AppFeature.Message.StreakRecoveryMessage(
                            StreakRecoveryFeature.Message.StreakRecoveryModalShownEventMessage
                        )
                    )
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
                R.layout.fragment_streak_recovery,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            streakRecoverySubtitle.text = params.modalText
            streakRecoveryGemsTextView.text = params.recoveryPriceGemsLabel
            streakRecoveryGemsAmountTextView.text = params.recoveryPriceAmountLabel
            streakRecoveryFirstTimeBadge.isVisible = params.isFirstTimeOffer
            streakRecoveryNextRecoveryPrice.isVisible = params.nextRecoveryPriceText != null
            if (params.nextRecoveryPriceText != null) {
                streakRecoveryNextRecoveryPrice.text = params.nextRecoveryPriceText
            }
            streakRecoveryBuyButton.setOnClickListener {
                viewModel.onNewMessage(
                    AppFeature.Message.StreakRecoveryMessage(
                        StreakRecoveryFeature.Message.RestoreStreakClicked
                    )
                )
            }
            streakRecoveryRejectButton.setOnClickListener {
                viewModel.onNewMessage(
                    AppFeature.Message.StreakRecoveryMessage(
                        StreakRecoveryFeature.Message.NoThanksClicked
                    )
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onNewMessage(
            AppFeature.Message.StreakRecoveryMessage(
                StreakRecoveryFeature.Message.StreakRecoveryModalHiddenEventMessage
            )
        )
    }
}