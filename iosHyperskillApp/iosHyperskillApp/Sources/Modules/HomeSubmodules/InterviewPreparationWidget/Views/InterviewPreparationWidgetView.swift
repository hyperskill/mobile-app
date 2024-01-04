import SwiftUI

extension InterviewPreparationWidgetView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.surface)
        let skeletonHeight: CGFloat = 114
    }
}

struct InterviewPreparationWidgetView: View {
    private(set) var appearance = Appearance()

    let viewStateKs: InterviewPreparationWidgetViewStateKs

    let viewModel: InterviewPreparationWidgetViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: viewModel.logViewedEvent
            )
            buildBody()
        }
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewStateKs {
        case .idle, .empty:
            EmptyView()
        case .loading(let data):
            if data.shouldShowSkeleton {
                SkeletonRoundedView()
                    .frame(height: appearance.skeletonHeight)
            } else {
                EmptyView()
            }
        case .error:
            InterviewPreparationWidgetErrorView(
                backgroundColor: appearance.backgroundColor,
                action: viewModel.doRetryContentLoading
            )
        case .content(let contentState):
            InterviewPreparationWidgetContentView(
                appearance: .init(backgroundColor: appearance.backgroundColor),
                countText: contentState.formattedStepsCount,
                description: contentState.description_,
                onTap: viewModel.doCallToAction
            )
        }
    }
}

extension InterviewPreparationWidgetView: Equatable {
    static func == (lhs: InterviewPreparationWidgetView, rhs: InterviewPreparationWidgetView) -> Bool {
        lhs.viewStateKs == rhs.viewStateKs
    }
}
