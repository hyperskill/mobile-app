package org.hyperskill.app.android.badges.view.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import org.hyperskill.app.android.R
import org.hyperskill.app.badges.domain.model.BadgeKind

@Composable
fun BadgeKind.getLockedPainter(): Painter? =
    when (this) {
        BadgeKind.ProjectMaster -> painterResource(id = R.drawable.badge_locked_project_mastery)
        BadgeKind.TopicMaster -> painterResource(id = R.drawable.badge_locked_topic_mastery)
        BadgeKind.CommittedLearner -> painterResource(id = R.drawable.badge_locked_comitted_learning)
        BadgeKind.BrilliantMind -> painterResource(id = R.drawable.badge_locked_brilliant_mind)
        BadgeKind.HelpingHand -> painterResource(id = R.drawable.badge_locked_helping_hand)
        BadgeKind.Sweetheart -> painterResource(id = R.drawable.badge_locked_sweet_heart)
        BadgeKind.Benefactor -> painterResource(id = R.drawable.badge_locked_benefactor)
        BadgeKind.BountyHunter -> painterResource(id = R.drawable.badge_locked_bounty_hunter)
        BadgeKind.UNKNOWN -> null
    }