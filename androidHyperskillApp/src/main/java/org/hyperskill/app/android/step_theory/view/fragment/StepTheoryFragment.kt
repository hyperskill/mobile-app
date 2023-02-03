package org.hyperskill.app.android.step_theory.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.appbar.AppBarLayout
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.adapter.decoration.VerticalMarginItemDecoration
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepTheoryBinding
import org.hyperskill.app.android.databinding.ItemStepCommentActionBinding
import org.hyperskill.app.android.databinding.ItemStepTheoryRatingBinding
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_theory.view.model.StepTheoryRating
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.CommentStatisticsEntry
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import kotlin.math.abs
import kotlin.math.floor

class StepTheoryFragment : Fragment(R.layout.fragment_step_theory) {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            StepTheoryFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }
    }

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var commentThreadTitleMapper: CommentThreadTitleMapper

    private val viewBinding: FragmentStepTheoryBinding by viewBinding(FragmentStepTheoryBinding::bind)
    private val stepTheoryRatingAdapter: DefaultDelegateAdapter<StepTheoryRating> = DefaultDelegateAdapter()
    private val stepCommentStatisticsAdapter: DefaultDelegateAdapter<CommentStatisticsEntry> = DefaultDelegateAdapter()

    private var step: Step by argument(serializer = Step.serializer())
    private var stepRoute: StepRoute by argument(serializer = StepRoute.serializer())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stepComponent = HyperskillApp.graph().buildStepComponent(stepRoute)
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
        commentThreadTitleMapper = stepComponent.commentThreadTitleMapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.stepTheoryAppBar.stepToolbar.root.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.stepTheoryAppBar.stepToolbar.stepToolbarTitle.text = step.title
        viewBinding.stepTheoryAppBar.root.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    viewBinding.stepTheoryFab.show()
                } else {
                    viewBinding.stepTheoryFab.hide()
                }
            }
        )
        viewBinding.stepTheoryFab.setOnClickListener {
            activity?.onBackPressed()
        }

        viewBinding.stepTheoryTimeToComplete.isVisible = step.secondsToComplete != null
        step.secondsToComplete?.let { secondsToComplete ->
            val minutesToComplete = floor(secondsToComplete / 60).toInt()
            viewBinding.stepTheoryTimeToComplete.text = resourceProvider.getString(
                SharedResources.strings.step_theory_reading_text,
                resourceProvider.getQuantityString(SharedResources.plurals.minutes, minutesToComplete, minutesToComplete)
            )
        }

        // ALTAPPS-397: Hidden
//        viewBinding.stepTheoryReactionTitle.text = buildSpannedString {
//            append(resources.getString(R.string.step_rating_text_part_1))
//            append(" ")
//            bold {
//                append(resources.getString(R.string.step_rating_text_part_2))
//            }
//        }
        initStepTheoryFragment(step)
        setupCommentStatisticsAdapterDelegate()
        // ALTAPPS-397: Hidden
        // setupStepRatingRecyclerView()
        // setupCommentStatisticsRecyclerView()
    }

    private fun initStepTheoryFragment(step: Step) {
        if (childFragmentManager.findFragmentByTag(STEP_CONTENT_FRAGMENT_TAG) == null) {
            val stepContentFragment = TextStepContentFragment.newInstance(step)

            childFragmentManager
                .beginTransaction()
                .add(R.id.stepTheoryContent, stepContentFragment, STEP_CONTENT_FRAGMENT_TAG)
                .commitNow()
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
        ViewCompat.setBackgroundTintList(viewBinding.stepTheoryRatingRecycler, AppCompatResources.getColorStateList(requireContext(), org.hyperskill.app.R.color.color_background))
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
                itemViewBinding.root.text = commentThreadTitleMapper.getFormattedStepCommentThreadStatistics(data.thread, data.totalCount)
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
}