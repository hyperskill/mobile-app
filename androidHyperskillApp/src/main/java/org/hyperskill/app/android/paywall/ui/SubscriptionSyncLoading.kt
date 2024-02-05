package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.centerWithVerticalBias
import org.hyperskill.app.R as SharedR

@Composable
fun SubscriptionSyncLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(PaywallDefaults.BackgroundColor)
    ) {
        Column(modifier = Modifier.align(Alignment.centerWithVerticalBias(-0.5f))) {
            Image(
                painter = painterResource(id = R.drawable.img_reload),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = SharedR.string.paywall_subscription_sync_description),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
private fun SubscriptionSyncLoadingPreview() {
    HyperskillTheme {
        SubscriptionSyncLoading()
    }
}