package org.hyperskill.app.android.topic_search.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.search.presentation.SearchFeature.SearchResultsViewState.Content.Item

@Composable
fun TopicSearchResultContent(
    items: List<Item>,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        content = {
            itemsIndexed(
                items = items,
                key = { _, item -> item.id }
            ) { index, item ->
                TopicSearchResultItem(
                    title = item.title,
                    onClick = remember {
                        {
                            onItemClick(item.id)
                        }
                    },
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(
                            top = TopicSearchResultDefaults.resultItemsVerticalSpacing,
                            bottom = if (index == items.lastIndex) {
                                TopicSearchResultDefaults.resultItemsVerticalSpacing
                            } else {
                                0.dp
                            },
                            start = TopicSearchResultDefaults.horizontalPadding,
                            end = TopicSearchResultDefaults.horizontalPadding
                        )
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun TopicSearchResultItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnClick by rememberUpdatedState(newValue = onClick)
    val cornerRadius = dimensionResource(id = R.dimen.corner_radius)
    Box(
        modifier
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = 1.dp,
                color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_12),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clickable(onClick = currentOnClick)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
            color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
        )
    }
}

@Preview
@Composable
private fun TopicSearchResultItemPreview() {
    HyperskillTheme {
        TopicSearchResultItem(
            title = "Basic data types",
            onClick = {

            }
        )
    }
}

@Preview
@Composable
private fun TopicSearchResultContentPreview() {
    HyperskillTheme {
        TopicSearchResultContent(
            items = List(3) {
                Item(
                    id = it.toLong(),
                    title = "Basic data types"
                )
            },
            onItemClick = {}
        )
    }
}