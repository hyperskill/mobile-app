package org.hyperskill.app.android.problems_limit.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentProblemsLimitInfoBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
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

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

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
        with(viewBinding) {
            problemsLimitInfoHomeButton.setOnClickListener {
                problemsLimitInfoModalViewModel.onGoHomeClicked()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        problemsLimitInfoModalViewModel.onHiddenEvent()
        super.onDismiss(dialog)
    }

    override fun render(state: ViewState) {
        viewBinding.problemsLimitInfoModalTitle.text = state.title
        viewBinding.problemsLimitInfoDescription.text = state.description

        with(viewBinding.problemsLimitInfoUnlimitedProblemsButton) {
            isVisible = state.unlockLimitsButtonText != null
            if (state.unlockLimitsButtonText != null) {
                text = state.unlockLimitsButtonText
                setOnClickListener {
                    problemsLimitInfoModalViewModel.onUnlockUnlimitedProblemsClicked()
                }
            }
        }
    }

    override fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NavigateTo.Home -> {
                requireRouter().backTo(MainScreen(Tabs.TRAINING))
                mainScreenRouter.switch(Tabs.TRAINING)
            }
            is ViewAction.NavigateTo.Paywall -> {
                requireRouter().navigateTo(PaywallScreen(action.paywallTransitionSource))
            }
        }
    }
}