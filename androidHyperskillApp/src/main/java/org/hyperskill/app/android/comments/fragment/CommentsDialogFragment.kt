package org.hyperskill.app.android.comments.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.comments.ui.Comments
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.comments.presentation.CommentsViewModel
import org.hyperskill.app.comments.screen.domain.model.CommentsScreenFeatureParams
import org.hyperskill.app.comments.screen.presentation.CommentsScreenFeature
import org.hyperskill.app.core.view.handleActions

class CommentsDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "CommentsDialogFragment"

        fun newInstance(params: CommentsScreenFeatureParams): CommentsDialogFragment =
            CommentsDialogFragment().apply {
                this.params = params
            }
    }

    private var params: CommentsScreenFeatureParams by argument(CommentsScreenFeatureParams.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val commentsViewModel: CommentsViewModel by viewModels { requireNotNull(viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponent()
        commentsViewModel.handleActions(this, ::handleActions)
    }

    private fun injectComponent() {
        val component = HyperskillApp.graph().buildPlatformCommentsScreenComponent(params)
        viewModelFactory = component.reduxViewModelFactory
    }

    override fun onStart() {
        super.onStart()
        dialog
            ?.window
            ?.let { window ->
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.setWindowAnimations(R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setOnShowListener {
                commentsViewModel.onNewMessage(CommentsScreenFeature.Message.ViewedEventMessage)
            }
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
                    Comments(
                        viewModel = commentsViewModel,
                        onCloseClick = ::onCloseClick
                    )
                }
            }
        }

    private fun onCloseClick() {
        dismiss()
    }

    private fun handleActions(action: CommentsScreenFeature.Action.ViewAction) {
        // no op
    }
}