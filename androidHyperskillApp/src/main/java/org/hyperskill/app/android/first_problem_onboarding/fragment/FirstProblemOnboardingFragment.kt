package org.hyperskill.app.android.first_problem_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme

class FirstProblemOnboardingFragment : Fragment() {
    companion object {
        fun newInstance(): FirstProblemOnboardingFragment =
            FirstProblemOnboardingFragment()
    }

    /*private var viewModelFactory: ViewModelProvider.Factory? = null
    private val viewModel: FirstProblemOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*injectComponent()
        viewModel.handleActions(this, block = ::onAction)*/
    }

    /*private fun injectComponent() {
        val component =
            HyperskillApp.graph().buildPlatformFirstProblemOnboardingComponent()
        viewModelFactory = component.reduxViewModelFactory
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme(
                    setTextColors = true,
                    setDefaultFontFamily = true
                ) {
                    // TODO: add your compose UI here
                }
            }
        }

    /*private fun onAction(action: ViewAction) {
        when (action) {

        }
    }*/
}