package org.hyperskill.app.android.manage_subscription.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.compose.plus
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.paywall.ui.SubscriptionDetails
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.ViewState

@Composable
fun ManageSubscriptionContent(
    state: ViewState.Content,
    onManageSubscriptionClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorResource(id = R.color.layer_1))
            .padding(ManageSubscriptionDefaults.ContentPadding + padding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            SubscriptionHeader(validUntilFormatted = state.validUntilFormatted)
            PlanDetails()
            MobileOnlyWarning()
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (state.isManageButtonVisible) {
            HyperskillButton(
                onClick = onManageSubscriptionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.manage_subscription_manage_btn))
            }
        }
    }
}

@Composable
private fun SubscriptionHeader(
    validUntilFormatted: String?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.manage_subscription_plan_title),
            style = MaterialTheme.typography.body2
        )
        Text(
            text = stringResource(id = R.string.manage_subscription_mobile_only),
            style = MaterialTheme.typography.h5
        )
        if (validUntilFormatted != null) {
            Text(
                text = validUntilFormatted,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
private fun PlanDetails(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.manage_subscription_plan_details_title),
            style = MaterialTheme.typography.h6,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        SubscriptionDetails()
    }
}

@Composable
private fun MobileOnlyWarning(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.manage_subscription_mobile_only_warning),
        color = colorResource(id = R.color.color_primary),
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = org.hyperskill.app.android.R.dimen.corner_radius)))
            .background(colorResource(id = R.color.color_overlay_blue_alpha_12))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    )
}

@Preview
@Composable
private fun SubscriptionHeaderPreview() {
    HyperskillTheme {
        SubscriptionHeader(
            validUntilFormatted = ManageSubscriptionPreviewDefaults.VALID_UNTIL_FORMATTED
        )
    }
}

@Preview
@Composable
fun MobileOnlyWarningPreview() {
    HyperskillTheme {
        MobileOnlyWarning()
    }
}

@Preview
@Composable
private fun ManageSubscriptionContentPreview() {
    HyperskillTheme {
        ManageSubscriptionContent(
            state = ViewState.Content(
                validUntilFormatted = ManageSubscriptionPreviewDefaults.VALID_UNTIL_FORMATTED,
                isManageButtonVisible = true
            ),
            onManageSubscriptionClick = {}
        )
    }
}