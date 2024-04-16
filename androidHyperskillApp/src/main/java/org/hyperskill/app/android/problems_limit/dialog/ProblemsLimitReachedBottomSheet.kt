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
import org.hyperskill.app.android.databinding.FragmentProblemsLimitReachedBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.problems_limit_reached.domain.ProblemsLimitReachedModalFeatureParams
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action.ViewAction
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProblemsLimitReachedBottomSheet : BottomSheetDialogFragment(), ReduxView<ViewState, ViewAction> {

    companion object {
        const val TAG = "ProblemsLimitReachedBottomSheet"

        fun newInstance(
            params: ProblemsLimitReachedModalFeatureParams
        ): ProblemsLimitReachedBottomSheet =
            ProblemsLimitReachedBottomSheet().apply {
                this.params = params
            }
    }

    private var params: ProblemsLimitReachedModalFeatureParams by argument(
        ProblemsLimitReachedModalFeatureParams.serializer()
    )

    private val viewBinding: FragmentProblemsLimitReachedBinding by viewBinding(
        FragmentProblemsLimitReachedBinding::bind
    )

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val problemsLimitReachedModalViewModel: ProblemsLimitReachedModalViewModel by reduxViewModel(this) {
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
                .buildPlatformProblemsLimitReachedModalComponent(params)
                .reduxViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    problemsLimitReachedModalViewModel.onShownEvent()
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
                R.layout.fragment_problems_limit_reached,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            problemsLimitReachedHomeButton.setOnClickListener {
                problemsLimitReachedModalViewModel.onGoHomeClicked()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        problemsLimitReachedModalViewModel.onHiddenEvent()
        super.onDismiss(dialog)
    }

    override fun render(state: ViewState) {
        viewBinding.problemsLimitReachedModalTitle.text = state.title
        viewBinding.problemsLimitReachedDescription.text = state.description

        with (viewBinding.problemsLimitReachedUnlimitedProblemsButton) {
            isVisible = state.unlockLimitsButtonText != null
            if (state.unlockLimitsButtonText != null) {
                text = state.unlockLimitsButtonText
                setOnClickListener {
                    problemsLimitReachedModalViewModel.onUnlockUnlimitedProblemsClicked()
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