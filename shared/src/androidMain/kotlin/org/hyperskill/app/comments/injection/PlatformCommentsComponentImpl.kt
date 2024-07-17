package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.presentation.CommentsViewModel
import org.hyperskill.app.comments.screen.injection.CommentsScreenComponent
import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory

class PlatformCommentsComponentImpl(
    private val commentsScreenComponent: CommentsScreenComponent
) : PlatformCommentsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                CommentsViewModel::class.java to {
                    CommentsViewModel(commentsScreenComponent.commentsScreenFeature.wrapWithFlowView())
                }
            )
        )
}