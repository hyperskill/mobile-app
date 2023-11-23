package org.hyperskill.app.android.challenge.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HtmlText
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun ChallengeDescription(
    description: String,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    HtmlText(
        text = description,
        modifier = modifier,
        baseSpanStyle = SpanStyle(color = colorResource(id = R.color.color_on_surface_alpha_60)),
        style = MaterialTheme.typography.body1,
        isHighlightLink = true,
        linkColor = colorResource(id = R.color.color_overlay_blue),
        onUrlClick = onLinkClick
    )
}

@Preview(showBackground = true)
@Composable
fun ChallengeDescriptionPreview() {
    HyperskillTheme {
        ChallengeDescription(
            description = ChallengeCardPreviewValues.DESCRIPTION,
            onLinkClick = {}
        )
    }
}