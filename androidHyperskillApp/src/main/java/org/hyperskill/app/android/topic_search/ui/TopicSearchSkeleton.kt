package org.hyperskill.app.android.topic_search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading

@Composable
fun TopicSearchSkeleton(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = TopicSearchResultDefaults.resultItemsVerticalSpacing)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(TopicSearchResultDefaults.resultItemsVerticalSpacing)
    ) {
        repeat(5) {
            TopicSearchSkeletonItem(
                modifier = Modifier.padding(horizontal = TopicSearchResultDefaults.horizontalPadding)
            )
        }
    }
}

@Composable
fun TopicSearchSkeletonItem(
    modifier: Modifier = Modifier
) {
    ShimmerLoading(
        modifier = modifier.requiredHeight(50.dp)
    )
}

@Preview
@Composable
fun TopicSearchSkeletonPreview() {
    HyperskillTheme {
        TopicSearchSkeleton(
            PaddingValues(bottom = 20.dp)
        )
    }
}