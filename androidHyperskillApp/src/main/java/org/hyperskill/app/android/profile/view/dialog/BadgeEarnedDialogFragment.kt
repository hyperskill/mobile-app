package org.hyperskill.app.android.profile.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.size.Scale
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentBadgeEarnedBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.badges.domain.model.Badge
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature

class BadgeEarnedDialogFragment : AbstractBadgeDialogFragment() {

    companion object {
        const val TAG: String = "BadgeEarnedDialogFragment"
        fun newInstance(badge: Badge): BadgeEarnedDialogFragment =
            BadgeEarnedDialogFragment().apply {
                this.badge = badge
            }
    }

    private var badge: Badge by argument(Badge.serializer())

    private val viewBinding: FragmentBadgeEarnedBinding by viewBinding(FragmentBadgeEarnedBinding::bind)

    private val viewModel: MainViewModel by viewModels(ownerProducer = ::requireActivity)

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
                        AppFeature.Message.NotificationClickHandlingMessage(
                            NotificationClickHandlingFeature.Message.EarnedBadgeModalShownEventMessage(badge.kind)
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
                R.layout.fragment_badge_earned,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewState = viewStateMapper.mapToEarnedBadgeModalViewState(badge)
        with(viewBinding) {
            modalTitle.text = viewState.title
            modalDescription.text = viewState.description
            with(badgeRank) {
                text = viewState.formattedRank
                setTextColor(getRankTextColor(requireContext(), viewState.rank))
            }
            badgeImage.load(data = getImageData(viewState.image, viewState.kind)) {
                scale(Scale.FIT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onNewMessage(
            AppFeature.Message.NotificationClickHandlingMessage(
                NotificationClickHandlingFeature.Message.EarnedBadgeModalHiddenEventMessage(badge.kind)
            )
        )
    }
}