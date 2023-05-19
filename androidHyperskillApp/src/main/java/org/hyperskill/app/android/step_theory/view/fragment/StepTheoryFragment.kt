package org.hyperskill.app.android.step_theory.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import kotlin.math.abs
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepTheoryBinding
import org.hyperskill.app.android.databinding.ItemStepCommentActionBinding
import org.hyperskill.app.android.databinding.ItemStepTheoryRatingBinding
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.android.step.view.model.StepCompletionView
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_theory.view.model.StepTheoryRating
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.CommentStatisticsEntry
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.argument

class StepTheoryFragment : Fragment(R.layout.fragment_step_theory), StepCompletionView {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        fun newInstance(step: Step, stepRoute: StepRoute, isPracticingAvailable: Boolean): Fragment =
            StepTheoryFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
                this.isPracticingAvailable = isPracticingAvailable
            }
    }

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dateFormatter: SharedDateFormatter
    private lateinit var commentThreadTitleMapper: CommentThreadTitleMapper

    private val viewBinding: FragmentStepTheoryBinding by viewBinding(FragmentStepTheoryBinding::bind)
    private val stepTheoryRatingAdapter: DefaultDelegateAdapter<StepTheoryRating> = DefaultDelegateAdapter()
    private val stepCommentStatisticsAdapter: DefaultDelegateAdapter<CommentStatisticsEntry> = DefaultDelegateAdapter()

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())
    private var isPracticingAvailable: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent(stepRoute)
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
        dateFormatter = HyperskillApp.graph().commonComponent.dateFormatter
        commentThreadTitleMapper = stepComponent.commentThreadTitleMapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding.stepTheoryAppBar) {
            stepToolbar.root.setNavigationOnClickListener {
                requireRouter().exit()
            }
            stepToolbar.stepToolbarTitle.text = step.title
            root.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    viewBinding.stepTheoryFab.show()
                } else {
                    viewBinding.stepTheoryFab.hide()
                }
            }
        }
        viewBinding.stepTheoryFab.setOnClickListener {
            requireRouter().exit()
        }

        setupStarPracticeButton(viewBinding.stepTheoryPracticeActionBeginning, isPracticingAvailable)
        setupStarPracticeButton(viewBinding.stepTheoryPracticeActionEnd, isPracticingAvailable)

        renderSecondsToComplete(step.secondsToComplete)

        // ALTAPPS-397: Hidden
//        viewBinding.stepTheoryReactionTitle.text = buildSpannedString {
//            append(resources.getString(R.string.step_rating_text_part_1))
//            append(" ")
//            bold {
//                append(resources.getString(R.string.step_rating_text_part_2))
//            }
//        }
        setupStepContentFragment(step)
        setupCommentStatisticsAdapterDelegate()
        // ALTAPPS-397: Hidden
        // setupStepRatingRecyclerView()
        // setupCommentStatisticsRecyclerView()
    }

    private fun setupStarPracticeButton(button: MaterialButton, isPracticingAvailable: Boolean) {
        button.isVisible = isPracticingAvailable
        button.setOnClickListener {
            parentOfType(StepCompletionHost::class.java)
                ?.onNewMessage(StepCompletionFeature.Message.StartPracticingClicked)
        }
    }

    private fun renderSecondsToComplete(secondsToComplete: Float?) {
        with(viewBinding.stepTheoryTimeToComplete) {
            isVisible = secondsToComplete != null
            if (secondsToComplete != null) {
                text = resourceProvider.getString(
                    SharedResources.strings.step_theory_reading_text,
                    dateFormatter.hoursWithMinutesCount(secondsToComplete)
                )
            }
        }
    }

    private fun setupStepContentFragment(step: Step) {
        setChildFragment(R.id.stepTheoryContent, STEP_CONTENT_FRAGMENT_TAG) {
            TextStepContentFragment.newInstance(step)
        }
    }

    private fun setupStepRatingAdapterDelegate() {
        stepTheoryRatingAdapter += adapterDelegate(
            layoutResId = R.layout.item_step_theory_rating
        ) {
            val itemViewBinding: ItemStepTheoryRatingBinding = ItemStepTheoryRatingBinding.bind(this.itemView)

            onBind { data ->
                itemViewBinding.root.setImageResource(data.drawableRes)
            }
        }
    }

    private fun setupStepRatingRecyclerView() {
        ViewCompat.setBackgroundTintList(
            viewBinding.stepTheoryRatingRecycler,
            AppCompatResources.getColorStateList(requireContext(), org.hyperskill.app.R.color.color_background)
        )
        with(viewBinding.stepTheoryRatingRecycler) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = stepTheoryRatingAdapter
            addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.step_theory_rating_horizontal_item_margin),
                    resources.getDimensionPixelSize(R.dimen.step_theory_rating_horizontal_edge_item_margin),
                    resources.getDimensionPixelSize(R.dimen.step_theory_rating_horizontal_edge_item_margin)
                )
            )
        }
    }

    private fun setupCommentStatisticsAdapterDelegate() {
        stepCommentStatisticsAdapter += adapterDelegate(
            layoutResId = R.layout.item_step_comment_action
        ) {
            val itemViewBinding: ItemStepCommentActionBinding = ItemStepCommentActionBinding.bind(this.itemView)

            onBind { data ->
                itemViewBinding.root.text =
                    commentThreadTitleMapper.getFormattedStepCommentThreadStatistics(data.thread, data.totalCount)
            }
        }
    }

    private fun setupCommentStatisticsRecyclerView() {
        with(viewBinding.stepTheoryCommentStatisticsRecycler) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = stepCommentStatisticsAdapter
            addItemDecoration(
                VerticalMarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.step_theory_comments_statistics_vertical_item_margin)
                )
            )
        }
    }

    override fun render(isPracticingLoading: Boolean) {
        if (isResumed) {
            with(viewBinding) {
                stepTheoryPracticeActionBeginningShimmer.isVisible = isPracticingLoading
                stepTheoryPracticeActionBeginning.isEnabled = !isPracticingLoading
                stepTheoryPracticeActionEndShimmer.isVisible = isPracticingLoading
                stepTheoryPracticeActionEnd.isEnabled = !isPracticingLoading
            }
        }
    }
}