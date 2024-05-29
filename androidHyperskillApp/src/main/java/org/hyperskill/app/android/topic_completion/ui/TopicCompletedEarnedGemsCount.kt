package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R

@Composable
fun TopicCompletedEarnedGemsCount(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = text,
            fontSize = 15.sp,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = org.hyperskill.app.android.R.drawable.ic_topic_completed_gems),
            contentDescription = null
        )
    }
}