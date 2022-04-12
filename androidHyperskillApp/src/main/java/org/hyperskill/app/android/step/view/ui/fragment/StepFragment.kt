package org.hyperskill.app.android.step.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResourcesFormatted
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepTheoryBinding
import org.hyperskill.app.android.databinding.ItemStepTheoryRatingBinding
import org.hyperskill.app.android.step.view.model.StepTheoryRating
import org.hyperskill.app.android.step.view.ui.adapter.decoration.RatingItemDecoration
import org.hyperskill.app.step.presentation.StepFeature
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.presentation.redux.container.ReduxView

class StepFragment :
    Fragment(R.layout.fragment_step_theory),
    ReduxView<StepFeature.State, StepFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): StepFragment =
            StepFragment()
    }

    private val viewBinding: FragmentStepTheoryBinding by viewBinding(FragmentStepTheoryBinding::bind)
    private val stepTheoryRatingAdapter: DefaultDelegateAdapter<StepTheoryRating> = DefaultDelegateAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        stepTheoryRatingAdapter.items = listOf(
            StepTheoryRating.EXCELLENT,
            StepTheoryRating.GOOD,
            StepTheoryRating.SATISFACTORY,
            StepTheoryRating.WEAK,
            StepTheoryRating.POOR
        )
        stepTheoryRatingAdapter += adapterDelegate(
            layoutResId = R.layout.item_step_theory_rating
        ) {
            val itemViewBinding: ItemStepTheoryRatingBinding = ItemStepTheoryRatingBinding.bind(this.itemView)

            onBind { data ->
                itemViewBinding.root.setImageResource(data.drawableRes)
            }
        }
    }

    private fun injectComponent() {
        HyperskillApp
            .component()
            .stepComponentBuilder()
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.stepTheoryTimeToComplete.text = SharedResourcesFormatted.getStepTheoryTimeRemaining(3).toString(requireContext())
        viewBinding.stepTheoryContent.text = TEMPLATE_TEXT
        viewBinding.stepTheoryReactionTitle.text = buildSpannedString {
            append(resources.getString(R.string.step_theory_reaction_subtitle_part_1))
            append(" ")
            bold {
                append(resources.getString(R.string.step_theory_reaction_subtitle_part_2))
            }
        }

        ViewCompat.setBackgroundTintList(viewBinding.stepTheoryRatingRecycler, AppCompatResources.getColorStateList(requireContext(), R.color.color_on_surface_alpha_25))
        viewBinding.stepTheoryRatingRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        viewBinding.stepTheoryRatingRecycler.adapter = stepTheoryRatingAdapter
        viewBinding.stepTheoryRatingRecycler.addItemDecoration(
            RatingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.step_theory_rating_horizontal_edge_margin),
                resources.getDimensionPixelSize(R.dimen.step_theory_rating_horizontal_margin),
                resources.getDimensionPixelSize(R.dimen.step_theory_rating_vertical_margin)
            )
        )
    }

    override fun onAction(action: StepFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: StepFeature.State) {
        // no op
    }
}

private val TEMPLATE_TEXT = """
    Programs in which there's nothing to calculate are quite rare. 
    Therefore, learning to program with numbers is never a bad idea. 
    An even more valuable skill we are about to learn is the processing of user data. 
    With its help, you can create interactive and by far more flexible applications. 
    So let's get started!
    
    Programs in which there's nothing to calculate are quite rare. 
    Therefore, learning to program with numbers is never a bad idea. 
    An even more valuable skill we are about to learn is the processing of user data. 
    With its help, you can create interactive and by far more flexible applications. 
    So let's get started!
    
    Programs in which there's nothing to calculate are quite rare. 
    Therefore, learning to program with numbers is never a bad idea. 
    An even more valuable skill we are about to learn is the processing of user data. 
    With its help, you can create interactive and by far more flexible applications. 
    So let's get started!
    
    Programs in which there's nothing to calculate are quite rare. 
    Therefore, learning to program with numbers is never a bad idea. 
    An even more valuable skill we are about to learn is the processing of user data. 
    With its help, you can create interactive and by far more flexible applications. 
    So let's get started!
""".trimIndent()