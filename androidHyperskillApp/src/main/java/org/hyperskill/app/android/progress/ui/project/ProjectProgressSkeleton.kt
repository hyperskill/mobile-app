package org.hyperskill.app.android.progress.ui.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.android.progress.ui.BlockHeaderSkeleton
import org.hyperskill.app.android.progress.ui.ProgressDefaults

@Composable
fun ProjectProgressSkeleton(
    modifier: Modifier = Modifier
) {
    val widgetModifier = remember {
        Modifier.height(ProgressDefaults.SkeletonWidgetHeight)
    }
    Column(modifier = modifier) {
        BlockHeaderSkeleton()
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        Row(horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.SmallSpaceDp)) {
            ShimmerLoading(
                radius = dimensionResource(id = R.dimen.corner_radius),
                modifier = widgetModifier.weight(1f)
            )
            ShimmerLoading(
                radius = dimensionResource(id = R.dimen.corner_radius),
                modifier = widgetModifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun TrackProjectSkeletonPreview() {
    ProjectProgressSkeleton()
}