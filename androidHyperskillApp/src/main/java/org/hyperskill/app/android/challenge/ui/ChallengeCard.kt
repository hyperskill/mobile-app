package org.hyperskill.app.android.challenge.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

private object ChallengeCardDefault {
    val paddingValues: PaddingValues = PaddingValues(20.dp)
}

object ChallengeCardPreviewDefault {
    const val TITLE = "Advent Streak Challenge"
    const val DATE_TEXT = "6 Oct - 12 Oct"
    const val DESCRIPTION = "Get ready to push your limits! Thrilling daily programming competition designed to test your coding skills and problem-solving abilities. What to Expect"
    const val TIME_TITLE = "Start in"
    const val TIME_SUBTITLE = "6 hrs 27 mins"
}

@Composable
fun AnnouncementChallengeCard(
    title: String,
    dateText: String,
    description: String,
    timeTitle: String,
    timeSubtitle: String
) {
    ChallengeScaffold {
        ChallengeHeader(
            title = title,
            dateText = dateText,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_announcment,
            modifier = Modifier.fillMaxWidth()
        )
        ChallengeDescription(description)
        ChallengeTimeText(title = timeTitle, subtitle = timeSubtitle)
    }
}

@Composable
private fun ChallengeScaffold(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    HyperskillCard(
        contentPadding = ChallengeCardDefault.paddingValues,
        onClick = {},
        modifier = modifier
    ) {
        Column(
            content = content,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
    }
}

@Composable
private fun ChallengeHeader(
    title: String,
    dateText: String,
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2
            )
            DateLabel(dateText)
        }
        Image(
            painter = painterResource(id = imageRes), 
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun DateLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colorResource(R.color.color_overlay_blue_alpha_12))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        style = MaterialTheme.typography.caption,
        color = colorResource(R.color.color_primary)
    )
}

@Composable
private fun ChallengeDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = description,
        modifier = modifier,
        style = MaterialTheme.typography.body1,
        color = colorResource(id = R.color.color_on_surface_alpha_60)
    )
}

@Composable
private fun ChallengeTimeText(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            fontSize = 14.sp,
            color = colorResource(id = R.color.color_on_surface_alpha_38)
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 14.sp,
            color = colorResource(id = R.color.color_on_surface_alpha_38)
        )
    }
}

@Preview
@Composable
private fun DateLabelPreview() {
    HyperskillTheme {
        DateLabel("6 Oct - 12 Oct")
    }
}

@Preview
@Composable
fun ChallengeHeaderPreview() {
    HyperskillTheme {
        ChallengeHeader(
            title = ChallengeCardPreviewDefault.TITLE,
            dateText = ChallengeCardPreviewDefault.DATE_TEXT,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_announcment
        )
    }
}

@Preview
@Composable
fun ChallengeDescriptionPreview() {
    HyperskillTheme {
        ChallengeDescription(
            description = ChallengeCardPreviewDefault.DESCRIPTION
        )
    }
}

@Preview
@Composable
fun ChallengeTimeTextPreview() {
    HyperskillTheme {
        ChallengeTimeText(
            title = ChallengeCardPreviewDefault.TIME_TITLE,
            subtitle = ChallengeCardPreviewDefault.TIME_SUBTITLE
        )
    }
}

@Preview()
@Composable
private fun AnnouncementChallengeCardPreview() {
    HyperskillTheme {
        AnnouncementChallengeCard(
            title = ChallengeCardPreviewDefault.TITLE,
            dateText = ChallengeCardPreviewDefault.DATE_TEXT,
            description = ChallengeCardPreviewDefault.DESCRIPTION,
            timeTitle = ChallengeCardPreviewDefault.TIME_TITLE,
            timeSubtitle = ChallengeCardPreviewDefault.TIME_SUBTITLE
        )
    }
}