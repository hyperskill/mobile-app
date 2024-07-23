package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.R as SharedR

@Composable
fun SubscriptionDetails(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SubscriptionOption(text = stringResource(id = SharedR.string.mobile_only_subscription_feature_1))
        SubscriptionOption(text = stringResource(id = SharedR.string.mobile_only_subscription_feature_2))
        SubscriptionOption(text = stringResource(id = SharedR.string.mobile_only_subscription_feature_3))
        SubscriptionOption(text = stringResource(id = SharedR.string.mobile_only_subscription_feature_4))
    }
}

@Composable
fun SubscriptionOption(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_paywall_option),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
private fun PaywallSubscriptionDetailsPreview() {
    HyperskillTheme {
        SubscriptionDetails()
    }
}