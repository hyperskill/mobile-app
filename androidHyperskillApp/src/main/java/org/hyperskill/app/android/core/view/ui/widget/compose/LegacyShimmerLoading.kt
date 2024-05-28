package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.ui.custom.LoadingView
import ru.nobird.android.view.base.ui.extension.toPx

@Composable
fun ShimmerLoading(
    modifier: Modifier = Modifier,
    radius: Dp = dimensionResource(id = R.dimen.corner_radius)
) {
    AndroidView(
        factory = { context ->
            LoadingView(context).apply {
                setRadius(
                    ru.nobird.android.view.base.ui.extension.Dp(radius.value).toPx().value
                )
            }
        },
        update = {
            it.setRadius(
                ru.nobird.android.view.base.ui.extension.Dp(radius.value).toPx().value
            )
        },
        modifier = modifier
    )
}