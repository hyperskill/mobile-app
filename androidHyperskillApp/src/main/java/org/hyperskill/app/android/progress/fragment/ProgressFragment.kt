package org.hyperskill.app.android.progress.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.progress.ui.ProgressScreen
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel

class ProgressFragment : Fragment() {

    companion object {
        fun newInstance(): ProgressFragment =
            ProgressFragment()
    }

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val progressScreenViewModel: ProgressScreenViewModel by viewModels { requireNotNull(viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val progressComponent = HyperskillApp.graph().buildPlatformProgressScreenComponent()
        viewModelFactory = progressComponent.reduxViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    ProgressScreen(progressScreenViewModel)
                }
            }
        }
}