import shared
import SwiftUI

extension TopicsRepetitionsView {
    struct Appearance {
        let padding = LayoutInsets.largeInset
        let backgroundColor = Color.systemGroupedBackground
        let skeletonHeight: CGFloat = 450
    }
}

struct TopicsRepetitionsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: TopicsRepetitionsViewModel

    @StateObject var pushRouter: SwiftUIPushRouter

    let dataMapper: TopicsRepetitionsViewDataMapper

    var body: some View {
        ScrollView {
            buildBody()
        }
        .background(appearance.backgroundColor)
        .navigationBarHidden(false)
        .navigationTitle(Strings.TopicsRepetitions.Card.titleUncompleted)
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            buildSkeletons()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            buildSkeletons()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadContent(forceUpdate: true)
                }
            )
        case .content(let state):
            let viewData = dataMapper.mapStateToViewData(state: state)

            VStack(spacing: appearance.padding) {
                TopicsRepetitionsChartBlock(
                    topicsToRepeatCount: Int(viewData.recommendedTopicsToRepeatCount),
                    repeatNextTopicText: viewData.repeatButtonText,
                    onRepeatNextTopicTap: {
                        viewModel.logClickedRepeatNextTopicEvent()

                        if let firstTopic = viewData.topicsToRepeat.first {
                            viewModel.doTopicStepQuizPresentation(stepID: firstTopic.stepId)
                        }
                    },
                    chartData: viewData.chartData.map { pair in
                        (String(pair.first ?? ""), Int(truncating: pair.second ?? 0))
                    },
                    chartDescription: viewData.chartDescription
                )
                .padding(.top, appearance.padding)

                TopicsRepetitionsRepeatBlock(
                    repeatBlockTitle: viewData.repeatBlockTitle,
                    trackTopicsTitle: viewData.trackTopicsTitle,
                    repeatButtons: viewData.topicsToRepeat.map { topic in
                        RepeatButtonInfo(
                            topicID: Int(topic.topicId),
                            title: topic.title,
                            onTap: {
                                viewModel.logClickedRepeatTopicEvent()

                                viewModel.doTopicStepQuizPresentation(stepID: topic.stepId)
                            }
                        )
                    },
                    showMoreButtonState: viewData.showMoreButtonState,
                    onShowMoreButtonTap: viewModel.doLoadNextTopics,
                    topicsToRepeatWillLoadedCount: Int(viewData.topicsToRepeatWillLoadedCount)
                )

                TopicsRepetitionsInfoBlock()
                    .padding(.bottom, appearance.padding)
            }
        }
    }

    @ViewBuilder
    private func buildSkeletons() -> some View {
        VStack(spacing: appearance.padding) {
            ForEach(0..<3) { _ in
                SkeletonRoundedView(appearance: SkeletonRoundedView.Appearance(cornerRadius: 0))
                    .frame(height: appearance.skeletonHeight)
            }
        }
        .padding(.vertical, appearance.padding)
    }

    private func handleViewAction(_ viewAction: TopicsRepetitionsFeatureActionViewAction) {
        switch TopicsRepetitionsFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        case .navigateTo(let navigateToViewAction):
            switch TopicsRepetitionsFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .stepScreen(let data):
                let assembly = StepAssembly(stepID: Int(data.stepId))
                pushRouter.pushViewController(assembly.makeModule())
            }
        }
    }
}

struct TopicsRepetitionsView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            TopicsRepetitionsAssembly().makeModule()
        }
    }
}
