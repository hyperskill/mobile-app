package org.hyperskill.app.android.challenge.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun ChallengeHeader(
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2,
                fontSize = 18.sp,
                color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_87)
            )
            ChallengeDateLabel(dateText)
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}

private class ChallengeImagePreviewProvider : PreviewParameterProvider<Int> {
    override val values: Sequence<Int>
        get() = sequenceOf(
            R.drawable.img_challenge_announcment,
            R.drawable.img_challenge_progress,
            R.drawable.img_challenge_completed,
            R.drawable.img_challenge_partly_completed,
            R.drawable.img_challenge_not_completed
        )
}

@Preview
@Composable
fun ChallengeHeaderPreview(
    @PreviewParameter(ChallengeImagePreviewProvider::class) imageRes: Int
) {
    HyperskillTheme {
        ChallengeHeader(
            title = ChallengeCardPreviewValues.TITLE,
            dateText = ChallengeCardPreviewValues.DATE_TEXT,
            imageRes = imageRes
        )
    }
}