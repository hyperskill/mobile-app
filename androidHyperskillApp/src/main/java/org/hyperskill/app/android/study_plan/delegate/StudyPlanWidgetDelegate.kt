package org.hyperskill.app.android.study_plan.delegate

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.DataLoadingErrorAdapterDelegate
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.databinding.ItemStudyPlanPaywallBinding
import org.hyperskill.app.android.study_plan.adapter.ActivityLoadingAdapterDelegate
import org.hyperskill.app.android.study_plan.adapter.StudyPlanActivityAdapterDelegate
import org.hyperskill.app.android.study_plan.adapter.StudyPlanItemAnimator
import org.hyperskill.app.android.study_plan.adapter.StudyPlanSectionAdapterDelegate
import org.hyperskill.app.android.study_plan.mapper.StudyPlanWidgetUIStateMapper
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

class StudyPlanWidgetDelegate(
    private val context: Context,
    private val onRetryContentLoadingClicked: () -> Unit,
    private val onNewMessage: (StudyPlanWidgetFeature.Message) -> Unit
) {

    private val studyPlanAdapter = DefaultDelegateAdapter<StudyPlanRecyclerItem>().apply {
        addDelegate(StudyPlanSectionAdapterDelegate(onNewMessage))
        addDelegate(
            StudyPlanActivityAdapterDelegate { activityId, sectionId ->
                onNewMessage(
                    StudyPlanWidgetFeature.Message.ActivityClicked(
                        activityId = activityId,
                        sectionId = sectionId
                    )
                )
            }
        )
        addDelegate(sectionsLoadingAdapterDelegate())
        addDelegate(loadAllTopicsButtonDelegate())
        addDelegate(expandCompletedActivitiesButtonDelegate())
        addDelegate(paywallAdapterDelegate())
        addDelegate(ActivityLoadingAdapterDelegate())
        addDelegate(
            DataLoadingErrorAdapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.ActivitiesError> { item ->
                onNewMessage(StudyPlanWidgetFeature.Message.RetryActivitiesLoading(item.sectionId))
            }
        )
    }

    private val uiStateMapper: StudyPlanWidgetUIStateMapper = StudyPlanWidgetUIStateMapper(context)

    private val sectionTopMargin =
        context.resources.getDimensionPixelOffset(R.dimen.study_plan_section_top_margin)
    private val activityTopMargin =
        context.resources.getDimensionPixelOffset(R.dimen.study_plan_activity_top_margin)

    private var studyPlanViewStateDelegate: ViewStateDelegate<StudyPlanWidgetViewState>? = null

    fun setup(recyclerView: RecyclerView, errorViewBinding: ErrorNoConnectionWithButtonBinding) {
        studyPlanViewStateDelegate = ViewStateDelegate<StudyPlanWidgetViewState>().apply {
            addState<StudyPlanWidgetViewState.Idle>()
            addState<StudyPlanWidgetViewState.Loading>(recyclerView)
            addState<StudyPlanWidgetViewState.Content>(recyclerView)
            addState<StudyPlanWidgetViewState.Error>(errorViewBinding.root)
        }

        errorViewBinding.tryAgain.setOnClickListener {
            onRetryContentLoadingClicked()
        }

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = studyPlanAdapter
        setupDecorations(context, recyclerView)
        recyclerView.itemAnimator = StudyPlanItemAnimator()
    }

    private fun setupDecorations(context: Context, recyclerView: RecyclerView) {
        val horizontalMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_horizontal_margin)
        val topMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_top_margin)
        val bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_bottom_margin)
        recyclerView.itemDecoration { position, rect, state ->
            rect.left = horizontalMargin
            rect.right = horizontalMargin
            rect.top = if (position == 0) {
                topMargin
            } else {
                val item = studyPlanAdapter.items.getOrNull(position)
                if (item != null) {
                    getTopMarginFor(item)
                } else {
                    0
                }
            }
            if (position == studyPlanAdapter.itemCount - 1) {
                state.willRunPredictiveAnimations()
                rect.bottom = bottomMargin
            }
        }
    }

    private fun getTopMarginFor(item: StudyPlanRecyclerItem): Int =
        when (item) {
            is StudyPlanRecyclerItem.SectionLoading,
            is StudyPlanRecyclerItem.Section,
            is StudyPlanRecyclerItem.PaywallBanner -> sectionTopMargin
            is StudyPlanRecyclerItem.ActivityLoading,
            is StudyPlanRecyclerItem.Activity,
            is StudyPlanRecyclerItem.ActivitiesError,
            is StudyPlanRecyclerItem.LoadAllTopicsButton,
            is StudyPlanRecyclerItem.ExpandCompletedActivitiesButton -> activityTopMargin
            else -> 0
        }

    fun cleanup() {
        studyPlanViewStateDelegate = null
    }

    fun render(state: StudyPlanWidgetViewState) {
        studyPlanViewStateDelegate?.switchState(state)
        studyPlanAdapter.items = uiStateMapper.map(state)
    }

    private fun sectionsLoadingAdapterDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.SectionLoading>(
            R.layout.item_study_plan_section_loading
        )

    private fun paywallAdapterDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.PaywallBanner>(R.layout.item_study_plan_paywall) {
            val binding = ItemStudyPlanPaywallBinding.bind(itemView)
            binding.studyPlanPaywallSubscribeButton.setOnClickListener {
                onNewMessage(StudyPlanWidgetFeature.Message.SubscribeClicked)
            }
        }

    private fun loadAllTopicsButtonDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.LoadAllTopicsButton>(
            R.layout.item_study_plan_load_more_button
        ) {
            itemView.setOnClickListener {
                val sectionId = item?.sectionId
                if (sectionId != null) {
                    onNewMessage(
                        StudyPlanWidgetFeature.Message.LoadMoreActivitiesClicked(sectionId)
                    )
                }
            }
        }

    private fun expandCompletedActivitiesButtonDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.ExpandCompletedActivitiesButton>(
            R.layout.item_study_plan_expand_completed_button
        ) {
            itemView.setOnClickListener {
                val sectionId = item?.sectionId
                if (sectionId != null) {
                    onNewMessage(
                        StudyPlanWidgetFeature.Message.ExpandCompletedActivitiesClicked(sectionId)
                    )
                }
            }
        }
}