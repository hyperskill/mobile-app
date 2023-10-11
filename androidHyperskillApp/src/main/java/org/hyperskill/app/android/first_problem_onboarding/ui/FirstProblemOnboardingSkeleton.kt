package org.hyperskill.app.android.first_problem_onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading

@Composable
fun FirstProblemOnboardingSkeleton() {
    FirstProblemOnboardingDefaults.ContentRootColumn {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            ShimmerLoading(
                radius = dimensionResource(id = R.dimen.corner_radius),
                modifier = Modifier
                    .width(160.dp)
                    .height(28.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(FirstProblemOnboardingDefaults.HeaderBottomPadding))
            ShimmerLoading(
                radius = dimensionResource(id = R.dimen.corner_radius),
                modifier = Modifier
                    .width(320.dp)
                    .height(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerLoading(
                radius = dimensionResource(id = R.dimen.corner_radius),
                modifier = Modifier
                    .width(280.dp)
                    .height(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        ShimmerLoading(
            radius = dimensionResource(id = R.dimen.corner_radius),
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.action_button_height))
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun FirstProblemOnboardingSkeletonPreview() {
    HyperskillTheme {
        FirstProblemOnboardingSkeleton()
    }
}