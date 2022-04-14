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
import com.google.android.material.appbar.AppBarLayout
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepTheoryBinding
import org.hyperskill.app.android.databinding.ItemStepTheoryRatingBinding
import org.hyperskill.app.android.step.view.model.StepTheoryRating
import org.hyperskill.app.android.step.view.ui.adapter.decoration.RatingItemDecoration
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.presentation.StepFeature
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.presentation.redux.container.ReduxView
import javax.inject.Inject
import kotlin.math.abs

class StepFragment :
    Fragment(R.layout.fragment_step_theory),
    ReduxView<StepFeature.State, StepFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): StepFragment =
            StepFragment()
    }

    @Inject
    internal lateinit var resourceProvider: ResourceProvider

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
        viewBinding.stepAppBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    viewBinding.fab.show()
                } else {
                    viewBinding.fab.hide()
                }
            }
        )
        viewBinding.stepTheoryTimeToComplete.text = resourceProvider.getString(
            SharedResources.strings.step_theory_reading_text,
            resourceProvider.getQuantityString(SharedResources.plurals.minutes, 3, 3)
        )
        viewBinding.stepTheoryContent.text = TEMPLATE_TEXT
        viewBinding.stepTheoryReactionTitle.text = buildSpannedString {
            append(resources.getString(R.string.step_rating_text_part_1))
            append(" ")
            bold {
                append(resources.getString(R.string.step_rating_text_part_2))
            }
        }

        ViewCompat.setBackgroundTintList(viewBinding.stepTheoryRatingRecycler, AppCompatResources.getColorStateList(requireContext(), R.color.color_background))
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