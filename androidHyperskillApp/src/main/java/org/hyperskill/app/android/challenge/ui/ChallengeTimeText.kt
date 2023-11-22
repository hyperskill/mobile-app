package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun ChallengeTimeText(
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
fun ChallengeTimeTextPreview() {
    HyperskillTheme {
        ChallengeTimeText(
            title = ChallengeCardPreviewValues.TIME_TITLE,
            subtitle = ChallengeCardPreviewValues.TIME_SUBTITLE
        )
    }
}