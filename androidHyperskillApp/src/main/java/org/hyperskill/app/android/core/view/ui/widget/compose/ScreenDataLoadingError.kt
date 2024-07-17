package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.R as SharedR

@Composable
fun ScreenDataLoadingError(
    modifier: Modifier = Modifier,
    errorMessage: String = stringResource(id = R.string.internet_problem),
    onRetryClick: () -> Unit
) {
    val currentOnRetryClick by rememberUpdatedState(newValue = onRetryClick)
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_placeholders))
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_wifi),
                contentDescription = errorMessage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_placeholders)))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = SharedR.color.color_on_background),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_placeholders)))
            HyperskillButton(
                onClick = currentOnRetryClick,
                modifier = Modifier
                    .widthIn(max = dimensionResource(id = R.dimen.auth_button_max_width))
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.reload))
            }
        }
    }
}

@Composable
@Preview
private fun ScreenDataLoadingErrorPreview() {
    HyperskillTheme {
        ScreenDataLoadingError {}
    }
}