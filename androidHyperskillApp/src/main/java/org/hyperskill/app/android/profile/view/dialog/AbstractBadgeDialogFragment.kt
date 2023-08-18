package org.hyperskill.app.android.profile.view.dialog

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.badges.domain.model.BadgeRank
import org.hyperskill.app.profile.view.BadgeImage
import org.hyperskill.app.profile.view.BadgesViewStateMapper

abstract class AbstractBadgeDialogFragment : BottomSheetDialogFragment() {
    protected val viewStateMapper: BadgesViewStateMapper by lazy(LazyThreadSafetyMode.NONE) {
        BadgesViewStateMapper(resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider)
    }

    @ColorInt
    protected fun getRankTextColor(context: Context, rank: BadgeRank): Int =
        when (rank) {
            BadgeRank.APPRENTICE,
            BadgeRank.EXPERT -> org.hyperskill.app.R.color.color_overlay_blue_brand
            BadgeRank.MASTER -> org.hyperskill.app.R.color.color_overlay_blue
            BadgeRank.LEGENDARY -> org.hyperskill.app.R.color.color_on_surface
            BadgeRank.LOCKED,
            BadgeRank.UNKNOWN -> org.hyperskill.app.R.color.color_on_surface_alpha_38
        }.let { colorRes -> ContextCompat.getColor(context, colorRes) }

    protected fun getImageData(image: BadgeImage, kind: BadgeKind): Any? =
        when (image) {
            BadgeImage.Locked -> when (kind) {
                BadgeKind.ProjectMaster -> R.drawable.img_badge_details_project_master
                BadgeKind.TopicMaster -> R.drawable.img_badge_details_topic_master
                BadgeKind.CommittedLearner -> R.drawable.img_badge_details_committed_learner
                BadgeKind.BrilliantMind -> R.drawable.img_badge_details_brilliant_mind
                BadgeKind.HelpingHand -> R.drawable.img_badge_details_helping_hand
                BadgeKind.Sweetheart -> R.drawable.img_badge_details_sweetheart
                BadgeKind.Benefactor -> R.drawable.img_badge_details_benefactor
                BadgeKind.BountyHunter -> R.drawable.img_badge_details_bounty_hunter
                BadgeKind.UNKNOWN -> null
            }
            is BadgeImage.Remote -> image.source
        }
}