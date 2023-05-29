package org.hyperskill.app.android.placeholder_new_user.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import coil.ImageLoader
import coil.load
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentNewUserTrackDetailsBinding
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import org.hyperskill.app.placeholder_new_user.view.model.PlaceholderNewUserViewData

class NewUserTrackDetailsBottomSheet : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "NewUserTrackDetailsBottomSheet"
        private const val TRACK_KEY = "track_key"

        fun newInstance(track: PlaceholderNewUserViewData.Track): NewUserTrackDetailsBottomSheet =
            NewUserTrackDetailsBottomSheet().apply {
                this.arguments = Bundle().apply {
                    putParcelable(
                        TRACK_KEY,
                        track,
                        serializer = PlaceholderNewUserViewData.Track.serializer()
                    )
                }
            }
    }

    private var track: PlaceholderNewUserViewData.Track? = null

    private val svgImageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    private val placeholderNewUserViewModel: PlaceholderNewUserViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        track = requireNotNull(
            requireArguments().getParcelable(
                TRACK_KEY,
                deserializer = PlaceholderNewUserViewData.Track.serializer()
            )
        ) {
            "Track can not be null"
        }
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    track?.id?.let { trackId ->
                        placeholderNewUserViewModel.onNewMessage(
                            PlaceholderNewUserFeature.Message.TrackModalShownEventMessage(trackId)
                        )
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hack to apply AppTheme to content
        // Without contextThemeWrapper AppTheme is not applied
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(R.layout.fragment_new_user_track_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewBinding = FragmentNewUserTrackDetailsBinding.bind(view)
        track?.let { track ->
            with(viewBinding) {
                trackDetailsStartLearningButton.setOnClickListener {
                    placeholderNewUserViewModel.onNewMessage(
                        PlaceholderNewUserFeature.Message.StartLearningButtonClicked(track.id)
                    )
                }
                trackDetailsTitleTextView.text = track.title
                trackDetailsDescriptionTextView.text = track.description
                trackDetailsDurationTextView.text = track.timeToComplete
                trackDetailsRatingTextView.text = track.rating
                trackDetailsIconImageView.load(track.imageSource, svgImageLoader)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        track?.id?.let { trackId ->
            placeholderNewUserViewModel.onNewMessage(
                PlaceholderNewUserFeature.Message.TrackModalHiddenEventMessage(trackId)
            )
        }
    }
}