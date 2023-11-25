package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidViewBinding
import org.hyperskill.app.R
import org.hyperskill.app.android.databinding.WidgetDataLoadingErrorBinding

@Composable
fun WidgetDataLoadingError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    AndroidViewBinding(
        WidgetDataLoadingErrorBinding::inflate,
        modifier
    ) {
        reloadButton.setOnClickListener {
            onRetryClick()
        }
        dataLoadingErrorTitle.text =
            title ?: root.context.getString(R.string.study_plan_activities_error_text)
    }
}

@Preview
@Composable
private fun WidgetDataLoadingErrorPreview() {
    HyperskillTheme {
        WidgetDataLoadingError(onRetryClick = {})
    }
}