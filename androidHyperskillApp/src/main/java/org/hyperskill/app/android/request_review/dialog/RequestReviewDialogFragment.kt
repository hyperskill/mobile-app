package org.hyperskill.app.android.request_review.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.hyperskill.app.android.core.extensions.launchUrlInCustomTabs
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.request_review.ui.RequestReviewDialog
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature
import org.hyperskill.app.request_review.presentation.RequestReviewModalViewModel
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.android.view.base.ui.extension.argument

class RequestReviewDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "RequestUserReviewDialogFragment"

        fun newInstance(stepRoute: StepRoute): RequestReviewDialogFragment =
            RequestReviewDialogFragment().apply {
                this.stepRoute = stepRoute
                this.shouldBeDismissedOnStart = false
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

    private var shouldBeDismissedOnStart: Boolean by argument()

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

    override fun onStart() {
        super.onStart()
        if (shouldBeDismissedOnStart) {
            dismiss()
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
            RequestReviewModalFeature.Action.ViewAction.Dismiss -> dismissIfResumed()
            RequestReviewModalFeature.Action.ViewAction.RequestUserReview -> requestUserReview()
            is RequestReviewModalFeature.Action.ViewAction.SubmitSupportRequest -> {
                launchUrlInCustomTabs(action.url, logger)
                dismissIfResumed()
            }
        }
    }

    private fun requestUserReview() {
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
                dismissIfResumed()
                return@launch
            }
            manager.launchReview(requireActivity(), reviewInfo)
            dismissIfResumed()
        }
    }

    private fun dismissIfResumed() {
        if (isResumed) {
            dismiss()
        } else {
            shouldBeDismissedOnStart = true
        }
    }
}