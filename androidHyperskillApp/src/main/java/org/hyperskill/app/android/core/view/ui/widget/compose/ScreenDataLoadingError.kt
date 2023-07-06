package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding

@Composable
fun ScreenDataLoadingError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidViewBinding(
        ErrorNoConnectionWithButtonBinding::inflate,
        modifier = modifier
    ) {
        root.isVisible = true
        tryAgain.setOnClickListener {
            onRetryClick()
        }
    }
}