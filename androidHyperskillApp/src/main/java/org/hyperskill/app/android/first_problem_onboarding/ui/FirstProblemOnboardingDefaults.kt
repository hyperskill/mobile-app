package org.hyperskill.app.android.first_problem_onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R

object FirstProblemOnboardingDefaults {
    val ContentPadding: PaddingValues = PaddingValues(
        top = 24.dp,
        bottom = 32.dp,
        start = 20.dp,
        end = 20.dp
    )
    val ImageBottomPadding: Dp = 20.dp
    val ImageTopPadding: Dp = 32.dp
    val HeaderBottomPadding: Dp = 8.dp

    @Composable
    fun ContentRootColumn(
        modifier: Modifier = Modifier,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.layer_1))
                .padding(ContentPadding),
            verticalArrangement = Arrangement.spacedBy(ImageBottomPadding),
            content = content
        )
    }
}