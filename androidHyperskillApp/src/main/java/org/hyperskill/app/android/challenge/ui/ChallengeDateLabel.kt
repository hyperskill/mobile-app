package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun ChallengeDateLabel(
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

@Preview
@Composable
private fun DateLabelPreview() {
    HyperskillTheme {
        ChallengeDateLabel("6 Oct - 12 Oct")
    }
}