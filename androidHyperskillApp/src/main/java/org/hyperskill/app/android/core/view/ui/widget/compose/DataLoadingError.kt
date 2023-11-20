package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import org.hyperskill.app.android.databinding.WidgetDataLoadingErrorBinding

@Composable
fun WidgetDataLoadingError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidViewBinding(
        WidgetDataLoadingErrorBinding::inflate,
        modifier
    ) {
        this.reloadButton.setOnClickListener {
            onRetryClick()
        }
    }
}

@Preview
@Composable
private fun WidgetDataLoadingErrorPreview() {
    HyperskillTheme {
        WidgetDataLoadingError(onRetryClick = {})
    }
}