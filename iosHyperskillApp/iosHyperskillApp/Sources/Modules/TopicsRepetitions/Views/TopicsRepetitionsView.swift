import shared
import SwiftUI
import UIKit

extension TopicsRepetitionsView {
    struct Appearance {
        let padding = LayoutInsets.largeInset
        let backgroundColor = Color.systemGroupedBackground
        let skeletonHeight: CGFloat = UIScreen.main.bounds.height / 3
    }
}

struct TopicsRepetitionsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: TopicsRepetitionsViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    let dataMapper: TopicsRepetitionsViewDataMapper

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

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
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
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
            ScrollView {
                buildContent(viewData: dataMapper.mapStateToViewData(state: state))
            }
        }
    }

    @ViewBuilder
    private func buildSkeletons() -> some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: appearance.padding) {
                ForEach(0..<3) { _ in
                    SkeletonRoundedView(appearance: SkeletonRoundedView.Appearance(cornerRadius: 0))
                        .frame(height: appearance.skeletonHeight)
                }
            }
            .padding(.vertical, appearance.padding)
        }
    }

    @ViewBuilder
    private func buildContent(viewData: TopicsRepetitionsViewData) -> some View {
        VStack(spacing: appearance.padding) {
            TopicsRepetitionsStatusBlock(
                repetitionsStatus: viewData.repetitionsStatus,
                onRepeatNextTopicTap: {
                    viewModel.doRepeatNextTopic()
                }
            )
            .padding(.top, appearance.padding)

            TopicsRepetitionsChartBlock(
                chartData: viewData.chartData.map { pair in
                    (String(pair.first ?? ""), Int(truncating: pair.second ?? 0))
                },
                chartDescription: viewData.chartDescription
            )

            if !(viewData.repetitionsStatus is RepetitionsStatusAllTopicsRepeated) {
                TopicsRepetitionsRepeatBlock(
                    repeatBlockTitle: viewData.repeatBlockTitle,
                    trackTopicsTitle: viewData.trackTopicsTitle,
                    topicsToRepeatFromCurrentTrack: viewData.topicsToRepeatFromCurrentTrack,
                    topicsToRepeatFromOtherTracks: viewData.topicsToRepeatFromOtherTracks,
                    onTopicButtonTapped: { topicID in
                        viewModel.doRepeatTopic(topicID: topicID)
                    },
                    showMoreButtonState: viewData.showMoreButtonState,
                    onShowMoreButtonTap: viewModel.doLoadNextTopics,
                    topicsToRepeatWillLoadedCount: Int(viewData.topicsToRepeatWillLoadedCount)
                )
            }

            TopicsRepetitionsInfoBlock()
                .padding(.bottom, appearance.padding)
        }
    }

    private func handleViewAction(_ viewAction: TopicsRepetitionsFeatureActionViewAction) {
        switch TopicsRepetitionsFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        case .navigateTo(let navigateToViewAction):
            switch TopicsRepetitionsFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .stepScreen(let data):
                let assembly = StepAssembly(stepRoute: StepRouteRepeat(stepId: data.stepId))
                stackRouter.pushViewController(assembly.makeModule())
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
