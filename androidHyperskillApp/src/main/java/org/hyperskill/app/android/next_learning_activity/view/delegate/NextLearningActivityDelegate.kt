package org.hyperskill.app.android.next_learning_activity.view.delegate

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.DataLoadingErrorAdapterDelegate
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutNextLearningActivityBinding
import org.hyperskill.app.android.next_learning_activity.view.model.NextLearningActivityLoadingErrorRecyclerItem
import org.hyperskill.app.android.stage_implementation.view.dialog.UnsupportedStageBottomSheet
import org.hyperskill.app.android.study_plan.adapter.ActivityLoadingAdapterDelegate
import org.hyperskill.app.android.study_plan.adapter.StudyPlanActivityAdapterDelegate
import org.hyperskill.app.android.study_plan.delegate.LearningActivityTargetViewActionHandler
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
    context: Context,
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


    private val nextLearningActivityTitleTextColor: Int =
        ContextCompat.getColor(context, StudyPlanRecyclerItem.Activity.activeTextColorRes)
    private val nextLearningActivityEndIcon: Drawable? =
        ContextCompat.getDrawable(context, StudyPlanRecyclerItem.Activity.nextActivityIconRes)

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

    fun render(state: NextLearningActivityWidgetFeature.State) {
        val viewState = NextLearningActivityWidgetViewStateMapper.map(state.contentState)
        nextLearningActivityAdapter.items = when (viewState) {
            Idle, Empty -> emptyList()
            Loading -> listOf(StudyPlanRecyclerItem.ActivityLoading(0, 0))
            is Content -> listOf(mapContentToRecyclerItem(viewState))
            NetworkError -> listOf(NextLearningActivityLoadingErrorRecyclerItem)
        }
    }

    private fun mapContentToRecyclerItem(content: Content): StudyPlanRecyclerItem.Activity =
        StudyPlanRecyclerItem.Activity(
            id = content.id,
            title = content.title,
            subtitle = content.subtitle,
            titleTextColor = nextLearningActivityTitleTextColor,
            progress = content.progress,
            formattedProgress = content.formattedProgress,
            endIcon = nextLearningActivityEndIcon,
            isClickable = true,
            isIdeRequired = content.isIdeRequired
        )

    fun <TFragment> handleAction(
        fragment: TFragment,
        action: NextLearningActivityWidgetFeature.Action.ViewAction
    ) where TFragment : Fragment, TFragment : UnsupportedStageBottomSheet.Callback {
        when (action) {
            is NextLearningActivityWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget ->
                LearningActivityTargetViewActionHandler.handle(
                    fragment = fragment,
                    viewAction = action.viewAction
                )
        }
    }
}