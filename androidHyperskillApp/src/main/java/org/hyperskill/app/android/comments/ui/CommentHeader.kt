package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R

@Composable
fun CommentHeader(
    authorAvatar: String,
    authorFullName: String,
    formattedTime: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredSize(40.dp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.color_on_surface_alpha_12))
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = authorFullName,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 16.sp,
                color = colorResource(id = R.color.color_on_surface_alpha_60),
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
            if (formattedTime != null) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.body1,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_secondary),
                    lineHeight = 16.sp
                )
            }
        }
    }
}