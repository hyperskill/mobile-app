package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun PartlyCompletedChallengeCard(
    title: String,
    dateText: String,
    modifier: Modifier = Modifier
) {
    ChallengeScaffold(modifier) {
        ChallengeHeader(
            title = title,
            dateText = dateText,
            imageRes = org.hyperskill.app.android.R.drawable.img_challenge_partly_completed,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Close to victory, bonus bounty!",
            style = MaterialTheme.typography.subtitle1,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            fontWeight = FontWeight.Bold
        )
        HyperskillButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Collect Reward")
        }
    }
}

@Preview
@Composable
fun PartlyCompletedChallengeCardPreview() {
    HyperskillTheme {
        PartlyCompletedChallengeCard(
            title = ChallengeCardPreviewValues.TITLE,
            dateText = ChallengeCardPreviewValues.DATE_TEXT
        )
    }
}