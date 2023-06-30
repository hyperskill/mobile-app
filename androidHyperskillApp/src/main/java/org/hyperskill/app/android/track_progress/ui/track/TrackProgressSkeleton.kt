package org.hyperskill.app.android.track_progress.ui.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.android.track_progress.ui.BlockHeaderSkeleton
import org.hyperskill.app.android.track_progress.ui.ProgressDefaults

@Composable
fun TrackProgressSkeleton(
    modifier: Modifier = Modifier
) {
    val bigWidgetModifier = remember {
        Modifier
            .fillMaxWidth()
            .height(ProgressDefaults.SkeletonWidgetHeight)
    }
    Column(modifier = modifier) {
        BlockHeaderSkeleton()
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        ShimmerLoading(
            radius = dimensionResource(id = R.dimen.corner_radius),
            modifier = bigWidgetModifier
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.BigSpaceDp))
        ShimmerLoading(
            radius = dimensionResource(id = R.dimen.corner_radius),
            modifier = bigWidgetModifier
        )
        Spacer(modifier = Modifier.height(ProgressDefaults.SmallSpaceDp))
        Row(horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.SmallSpaceDp)) {
            ShimmerLoading(
                modifier = Modifier
                    .height(ProgressDefaults.SkeletonWidgetHeight)
                    .weight(1f)
            )
            ShimmerLoading(
                modifier = Modifier
                    .height(ProgressDefaults.SkeletonWidgetHeight)
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun TrackProgressSkeletonBlockPreview() {
    TrackProgressSkeleton()
}