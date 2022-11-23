import shared
import SwiftUI

extension TopicsRepetitionsView {
    struct Appearance {
        let padding = LayoutInsets.largeInset
        let backgroundColor = Color.systemGroupedBackground
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
            ProgressView()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            ProgressView()
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
                        if let firstTopic = viewData.topicsToRepeat.first {
                            viewModel.handleOpenStepRequested(stepID: firstTopic.stepId, topicID: firstTopic.topicId)
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
                                viewModel.handleOpenStepRequested(
                                    stepID: topic.stepId,
                                    topicID: topic.topicId
                                )
                            }
                        )
                    },
                    showMoreButtonState: viewData.showMoreButtonState,
                    onShowMoreButtonTap: viewModel.doLoadNextTopics
                )

                TopicsRepetitionsInfoBlock()
                    .padding(.bottom, appearance.padding)
            }
        }
    }

    private func handleViewAction(_ viewAction: TopicsRepetitionsFeatureActionViewAction) {
        switch TopicsRepetitionsFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        case .navigateTo(let navigateToViewAction):
            switch TopicsRepetitionsFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .stepScreen(let data):
                let assembly = StepAssembly(
                    stepID: Int(data.stepId),
                    onQuizCompleted: {
                        viewModel.onTopicRepeated(topicID: data.topicId)
                    }
                )
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
