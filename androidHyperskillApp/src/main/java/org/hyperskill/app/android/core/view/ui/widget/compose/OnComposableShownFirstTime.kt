package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun OnComposableShownFirstTime(
    key: Any?,
    block: () -> Unit
) {
    DisposableEffect(key) {
        block()
        onDispose {
            // no op
        }
    }
}