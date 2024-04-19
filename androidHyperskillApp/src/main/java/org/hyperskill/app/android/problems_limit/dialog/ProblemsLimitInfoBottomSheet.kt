package org.hyperskill.app.android.problems_limit.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import co.touchlab.kermit.Logger
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.logger
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentProblemsLimitInfoBinding
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action.ViewAction
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProblemsLimitInfoBottomSheet : BottomSheetDialogFragment(), ReduxView<ViewState, ViewAction> {

    companion object {
        const val TAG = "ProblemsLimitInfoBottomSheet"

        fun newInstance(
            params: ProblemsLimitInfoModalFeatureParams
        ): ProblemsLimitInfoBottomSheet =
            ProblemsLimitInfoBottomSheet().apply {
                this.params = params
            }
    }

    private var params: ProblemsLimitInfoModalFeatureParams by argument(
        ProblemsLimitInfoModalFeatureParams.serializer()
    )

    private val viewBinding: FragmentProblemsLimitInfoBinding by viewBinding(
        FragmentProblemsLimitInfoBinding::bind
    )

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val problemsLimitInfoModalViewModel: ProblemsLimitInfoModalViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private val logger: Logger by logger(TAG)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformProblemsLimitInfoModalComponent(params)
                .reduxViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    problemsLimitInfoModalViewModel.onShownEvent()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_problems_limit_info,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding.problemsLimitInfoUnlimitedProblemsButton) {
            setOnClickListener {
                problemsLimitInfoModalViewModel.onUnlockUnlimitedProblemsClicked()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        problemsLimitInfoModalViewModel.onHiddenEvent()
        super.onDismiss(dialog)
    }

    override fun render(state: ViewState) {
        with (viewBinding) {
            problemsLimitInfoModalTitle.text = state.title
            problemsLimitInfoDescription.text = state.limitsDescription

            problemsLimitInfoLeftLimits.isVisible = state.leftLimitsText != null
            problemsLimitInfoLeftLimits.text = state.leftLimitsText

            problemsLimitInfoResetIn.isVisible = state.resetInText != null
            problemsLimitInfoResetIn.text = state.resetInText

            problemsLimitInfoUnlockDescription.isVisible = state.unlockDescription != null
            problemsLimitInfoUnlockDescription.text = state.unlockDescription

            problemsLimitInfoUnlimitedProblemsButton.text = state.buttonText

            playAnimation(problemsLimitInfoAnimation, state.animation)

        }
    }

    override fun onAction(action: ViewAction) {
        when (action) {
            is ViewAction.NavigateTo.Paywall -> {
                requireRouter().navigateTo(PaywallScreen(action.paywallTransitionSource))
            }
        }
    }

    @Suppress("TooGenericExceptionCaught", "MagicNumber")
    private fun playAnimation(view: LottieAnimationView, animation: ViewState.Animation) {
        view.repeatMode = LottieDrawable.RESTART
        view.repeatCount = if (animation.isLooped) LottieDrawable.INFINITE else 0
        view.speed = if (animation == ViewState.Animation.FULL_LIMITS) 0.5f else 1f

        val animationRes = getAnimationRes(animation)
        try {
            view.setAnimation(animationRes)
            view.playAnimation()
        } catch (e: Exception) {
            logger.e(e) {
                "Failed to run animation for animation type = $animation"
            }
        }
    }

    @RawRes
    private fun getAnimationRes(animation: ViewState.Animation): Int =
        when (animation) {
            ViewState.Animation.FULL_LIMITS -> R.raw.problems_limit_full_limits_animation
            ViewState.Animation.PARTIALLY_SPENT_LIMITS -> R.raw.problems_limit_partially_spent_limits_animation
            ViewState.Animation.NO_LIMITS_LEFT -> R.raw.problems_limit_no_limits_left_animation
        }
}