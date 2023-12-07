package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding

@Composable
fun ScreenDataLoadingError(
    modifier: Modifier = Modifier,
    errorMessage: String = stringResource(id = R.string.internet_problem),
    onRetryClick: () -> Unit
) {
    AndroidViewBinding(
        ErrorNoConnectionWithButtonBinding::inflate,
        modifier = modifier
    ) {
        root.isVisible = true
        this.errorMessage.text = errorMessage
        tryAgain.setOnClickListener {
            onRetryClick()
        }
    }
}