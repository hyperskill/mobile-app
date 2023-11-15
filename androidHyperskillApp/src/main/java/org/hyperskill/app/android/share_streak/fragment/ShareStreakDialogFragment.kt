package org.hyperskill.app.android.share_streak.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentShareStreakBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import ru.nobird.android.view.base.ui.extension.argument

class ShareStreakDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ShareStreakBottomSheetTag"

        fun newInstance(streak: Int): ShareStreakDialogFragment =
            ShareStreakDialogFragment().apply {
                this.streak = streak
            }
    }

    private var streak: Int by argument()

    private val binding: FragmentShareStreakBinding by viewBinding(FragmentShareStreakBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    (parentFragment as? Callback)?.onShareStreakBottomSheetShown(streak)
                }
            }
        }

    override fun onDismiss(dialog: DialogInterface) {
        (parentFragment as? Callback)?.onShareStreakBottomSheetDismissed(streak)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(R.layout.fragment_share_streak, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            shareStreakImage.load(getImageRes(streak)) {
                transformations(
                    RoundedCornersTransformation(
                        requireContext().resources.getDimension(R.dimen.corner_radius)
                    )
                )
            }
            shareStreakShareButton.setOnClickListener {
                (parentFragment as? Callback)?.onShareClick(streak)
            }
            shareStreakRefuseButton.setOnClickListener {
                (parentFragment as? Callback)?.onRefuseStreakSharingClick(streak)
            }
        }
    }

    @DrawableRes
    private fun getImageRes(streak: Int): Int =
        when (streak) {
            1 -> R.drawable.img_share_streak_day_1
            5 -> R.drawable.img_share_streak_day_5
            10 -> R.drawable.img_share_streak_day_10
            25 -> R.drawable.img_share_streak_day_25
            50 -> R.drawable.img_share_streak_day_50
            100 -> R.drawable.img_share_streak_day_100
            else -> R.drawable.img_share_streak_day_1
        }

    interface Callback {
        fun onShareStreakBottomSheetShown(streak: Int)

        fun onShareStreakBottomSheetDismissed(streak: Int)

        fun onShareClick(streak: Int)

        fun onRefuseStreakSharingClick(streak: Int)
    }
}