package org.hyperskill.app.android.challenge.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun ChallengeDescription(
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

@Preview
@Composable
fun ChallengeDescriptionPreview() {
    HyperskillTheme {
        ChallengeDescription(
            description = ChallengeCardPreviewValues.DESCRIPTION
        )
    }
}