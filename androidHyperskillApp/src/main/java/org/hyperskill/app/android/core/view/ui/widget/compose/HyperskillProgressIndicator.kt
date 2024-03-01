package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import me.zhanghai.android.materialprogressbar.MaterialProgressBar

@Composable
fun HyperskillProgressBar(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            MaterialProgressBar(context)
        },
        modifier = modifier
    )
}