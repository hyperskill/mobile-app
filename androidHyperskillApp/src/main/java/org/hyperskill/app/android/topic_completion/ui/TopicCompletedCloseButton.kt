package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.hyperskill.app.android.R

@Composable
fun TopicCompletedCloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val actualOnClick by rememberUpdatedState(newValue = onClick)
    Box(
        modifier
            .requiredSize(TopicCompletedDefaults.CLOSE_BUTTON_SIZE)
            .clickable(onClick = actualOnClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_close_topic_completed),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}