import shared
import SwiftUI

final class CommentsAssembly: UIKitAssembly {
    private let params: CommentsScreenFeatureParams

    init(params: CommentsScreenFeatureParams) {
        self.params = params
    }

    func makeModule() -> UIViewController {
        let commentsScreenComponent =
            AppGraphBridge.sharedAppGraph.buildCommentsScreenComponent(params: params)

        let commentsScreenViewModel = CommentsViewModel(
            feature: commentsScreenComponent.commentsScreenFeature
        )

        let commentsScreenView = CommentsView(
            viewModel: commentsScreenViewModel
        )

        let hostingController = CommentsHostingController(
            rootView: commentsScreenView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        let commentThreadTitleMapper = commentsScreenComponent.commentThreadTitleMapper
        hostingController.title = commentThreadTitleMapper.getFormattedCommentThreadStatistics(
            thread: params.commentStatistics.thread,
            count: params.commentStatistics.totalCount
        )

        return hostingController
    }
}
