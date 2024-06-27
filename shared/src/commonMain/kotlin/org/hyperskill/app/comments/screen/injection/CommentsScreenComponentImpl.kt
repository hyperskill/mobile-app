package org.hyperskill.app.comments.screen.injection

import org.hyperskill.app.comments.screen.view.mapper.CommentThreadTitleMapper
import org.hyperskill.app.core.injection.AppGraph

internal class CommentsScreenComponentImpl(
    private val appGraph: AppGraph
) : CommentsScreenComponent {
    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)
}