package org.hyperskill.app.android.track_selection.details.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.doOnNextLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentTrackSelectionDetailsBinding

class TrackSelectionDetailsFragment : Fragment(R.layout.fragment_track_selection_details) {

    companion object {
        fun newInstance(): TrackSelectionDetailsFragment =
            TrackSelectionDetailsFragment()
    }

    private val viewBinding: FragmentTrackSelectionDetailsBinding by viewBinding(
        FragmentTrackSelectionDetailsBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.projectSelectionDetailsToolbar.setNavigationOnClickListener {
            requireRouter().exit()
        }
        updateContentBottomMargin()
    }

    private fun updateContentBottomMargin() {
        viewBinding.trackSelectionDetailsSelectButton.doOnNextLayout {
            viewBinding.trackSelectionDetailsContentContainer.updateLayoutParams<MarginLayoutParams> {
                updateMargins(
                    bottom = it.height + it.marginBottom +
                        resources.getDimensionPixelOffset(R.dimen.track_selection_details_content_bottom_margin)
                )
            }
        }
    }
}