package org.hyperskill.app.android.next_learning_activity.view.delegate

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.DataLoadingErrorAdapterDelegate
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutNextLearningActivityBinding
import org.hyperskill.app.android.next_learning_activity.view.model.NextLearningActivityLoadingErrorRecyclerItem
import org.hyperskill.app.android.study_plan.adapter.ActivityLoadingAdapterDelegate
import org.hyperskill.app.android.study_plan.adapter.StudyPlanActivityAdapterDelegate
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ViewState.Content
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ViewState.Empty
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ViewState.Idle
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ViewState.Loading
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ViewState.NetworkError
import org.hyperskill.app.next_learning_activity_widget.view.mapper.NextLearningActivityWidgetViewStateMapper
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class NextLearningActivityDelegate(
    onNewMessage: (NextLearningActivityWidgetFeature.Message) -> Unit
) {

    private val nextLearningActivityAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DefaultDelegateAdapter<StudyPlanRecyclerItem>().apply {
            addDelegate(
                StudyPlanActivityAdapterDelegate {
                    onNewMessage(NextLearningActivityWidgetFeature.Message.NextLearningActivityClicked)
                }
            )
            addDelegate(ActivityLoadingAdapterDelegate())
            addDelegate(
                DataLoadingErrorAdapterDelegate<StudyPlanRecyclerItem, NextLearningActivityLoadingErrorRecyclerItem> {
                    onNewMessage(NextLearningActivityWidgetFeature.Message.RetryContentLoading)
                }
            )
        }
    }

    fun setup(context: Context, viewBinding: LayoutNextLearningActivityBinding) {
        with(viewBinding) {
            with(homeNextLearningActivityRecycler) {
                adapter = nextLearningActivityAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    HorizontalMarginItemDecoration(
                        horizontalMargin = context.resources.getDimensionPixelOffset(R.dimen.screen_horizontal_padding)
                    )
                )
            }
        }
    }

    fun render(
        context: Context,
        state: NextLearningActivityWidgetFeature.State
    ) {
        val viewState = NextLearningActivityWidgetViewStateMapper.map(state.contentState)
        nextLearningActivityAdapter.items = when (viewState) {
            Idle, Empty -> emptyList()
            Loading -> listOf(StudyPlanRecyclerItem.ActivityLoading(0, 0))
            is Content -> listOf(mapContentToRecyclerItem(context, viewState))
            NetworkError -> listOf(NextLearningActivityLoadingErrorRecyclerItem)
        }
    }

    private fun mapContentToRecyclerItem(context: Context, content: Content): StudyPlanRecyclerItem.Activity =
        StudyPlanRecyclerItem.Activity(
            id = 0,
            title = content.title,
            subtitle = content.subtitle,
            titleTextColor = ContextCompat.getColor(context,StudyPlanRecyclerItem.Activity.activeTextColorRes),
            progress = content.progress,
            formattedProgress = content.formattedProgress,
            endIcon = ContextCompat.getDrawable(context, StudyPlanRecyclerItem.Activity.nextActivityIconRes),
            isClickable = true,
            isIdeRequired = content.isIdeRequired
        )
}