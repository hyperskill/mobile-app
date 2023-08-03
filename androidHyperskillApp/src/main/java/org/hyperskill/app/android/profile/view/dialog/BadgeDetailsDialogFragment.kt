package org.hyperskill.app.android.profile.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.size.Scale
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentBadgeDetailsBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.presentation.ProfileFeature.Action.ViewAction.BadgeDetails
import org.hyperskill.app.profile.view.BadgesViewStateMapper

class BadgeDetailsDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG: String = "BadgeDetailsDialogFragment"
        fun newInstance(badgeDetails: BadgeDetails): BadgeDetailsDialogFragment =
            BadgeDetailsDialogFragment().apply {
                this.badgeDetails = badgeDetails
            }
    }

    private var badgeDetails: BadgeDetails by argument(BadgeDetails.serializer())

    private val viewBinding: FragmentBadgeDetailsBinding by viewBinding(FragmentBadgeDetailsBinding::bind)

    private val viewStateMapper: BadgesViewStateMapper by lazy(LazyThreadSafetyMode.NONE) {
        BadgesViewStateMapper(resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    (parentFragment as? Callback)?.onBadgeDetailsDialogFragmentShown(badgeDetails.badgeKind)
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
                R.layout.fragment_badge_details,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewState = when (val details = badgeDetails) {
            is BadgeDetails.Badge -> viewStateMapper.mapToDetails(details.badge)
            is BadgeDetails.Kind -> viewStateMapper.mapToDetails(details.badgeKind)
        }
        with(viewBinding) {
            badgeTitle.text = viewState.title
            badgeDescription.text = viewState.badgeDescription
            badgeRank.text = viewState.rank
            badgeCurrentLevel.text = viewState.formattedCurrentLevel
            with(badgeNextLevel) {
                text = viewState.formattedNextLevel
                setCompoundDrawablesWithIntrinsicBounds(
                    /* left = */ if (viewState.isLocked) R.drawable.ic_badge_details_locked else 0,
                    /* top = */ 0,
                    /* right = */ 0,
                    /* bottom = */ 0
                )
            }
            badgeLevelProgressIndicator.progress =
                viewState.progress?.let { progress -> (progress * 100).roundToInt() } ?: 0
            badgeLevelDescription.text = viewState.levelDescription
            badgeImage.load(viewState.imageSource) {
                scale(Scale.FIT)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? Callback)?.onBadgeDetailsDialogFragmentHidden(badgeDetails.badgeKind)
    }

    interface Callback {
        fun onBadgeDetailsDialogFragmentShown(badgeKind: BadgeKind)

        fun onBadgeDetailsDialogFragmentHidden(badgeKind: BadgeKind)
    }
}