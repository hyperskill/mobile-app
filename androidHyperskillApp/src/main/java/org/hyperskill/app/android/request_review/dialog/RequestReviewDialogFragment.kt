package org.hyperskill.app.android.request_review.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import kotlinx.coroutines.launch
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.setHyperskillColors
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.request_review.ui.RequestReviewDialog
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature
import org.hyperskill.app.request_review.presentation.RequestReviewModalViewModel
import org.hyperskill.app.step.domain.model.StepRoute

class RequestReviewDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RequestUserReviewDialogFragment"

        fun newInstance(stepRoute: StepRoute): RequestReviewDialogFragment =
            RequestReviewDialogFragment().apply {
                this.stepRoute = stepRoute
            }
    }

    private var stepRoute: StepRoute by argument(StepRoute.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val requestUserReviewViewModal: RequestReviewModalViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private val logger: Logger by lazy {
        HyperskillApp.graph().loggerComponent.logger.withTag(TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    private fun injectComponent() {
        val requestReviewComponent = HyperskillApp.graph().buildPlatformRequestReviewComponent(stepRoute)
        viewModelFactory = requestReviewComponent.reduxViewModelFactory
        requestUserReviewViewModal.handleActions(this, onAction = ::onAction)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    requestUserReviewViewModal.onShownEvent()
                }
            }
        }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requestUserReviewViewModal.onHiddenEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    RequestReviewDialog(viewModel = requestUserReviewViewModal)
                }
            }
        }

    private fun onAction(action: RequestReviewModalFeature.Action.ViewAction) {
        when (action) {
            RequestReviewModalFeature.Action.ViewAction.Dismiss -> dismiss()
            RequestReviewModalFeature.Action.ViewAction.RequestUserReview -> {
                val manager = ReviewManagerFactory.create(requireContext())
                lifecycleScope.launch {
                    val reviewInfo = try {
                        manager.requestReview()
                    } catch (e: ReviewException) {
                        logger.e(e) {
                            val errorDescription = when (e.errorCode) {
                                ReviewErrorCode.NO_ERROR -> "No error"
                                ReviewErrorCode.PLAY_STORE_NOT_FOUND -> "Play store not found"
                                ReviewErrorCode.INTERNAL_ERROR -> "Internal error"
                                ReviewErrorCode.INVALID_REQUEST -> " Invalid request"
                                else -> ""
                            }
                            "Failed to launch app review. $errorDescription"
                        }
                        dismiss()
                        return@launch
                    }
                    manager.launchReview(requireActivity(), reviewInfo)
                    dismiss()
                }
            }
            is RequestReviewModalFeature.Action.ViewAction.SubmitSupportRequest -> {
                val intent = CustomTabsIntent.Builder()
                    .setHyperskillColors(requireContext())
                    .build()
                intent.launchUrl(requireActivity(), Uri.parse(action.url))
                dismiss()
            }
        }
    }
}