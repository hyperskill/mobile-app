package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R

@Composable
fun TopicCompletedTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.color_on_surface_alpha_87),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}