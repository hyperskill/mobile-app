import shared
import SwiftUI

struct StepView: View {
    @ObservedObject private var viewModel: StepViewModel

    init(viewModel: StepViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            makeContentView()
                .navigationTitle(makeNavigationTitleString())
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    @ViewBuilder
    private func makeContentView() -> some View {
        switch self.viewModel.state {
        case is StepFeatureStateIdle:
            ProgressView().onAppear(perform: viewModel.loadStep)
        case is StepFeatureStateLoading:
            ProgressView()
        case is StepFeatureStateError:
            Text("Error")
        case let data as StepFeatureStateData:
            ScrollView {
                VStack {
                    StepHeaderView(
                        title: data.step.type.capitalized,
                        timeToComplete: "3 minutes remaining"
                    )

                    StepActionButton(
                        title: Strings.stepStartPracticingText,
                        style: .greenOutline
                    ) {
                        print("Start practicing tapped")
                    }

                    Text(data.step.block.text)

                    StepBottomControlsView(
                        commentStatisticsViewData: data.step.commentsStatistics.map(\.viewData),
                        onStartPracticingClick: {
                            print("Start practicing tapped")
                        },
                        onCommentStatisticClick: { commentStatistic in
                            print("Comment statistic clicked = \(commentStatistic)")
                        }
                    )
                }
            }
        default:
            Text("HERE")
        }
    }

    private func makeNavigationTitleString() -> String {
        if let data = self.viewModel.state as? StepFeatureStateData {
            return data.step.title
        }
        return "Step"
    }

    private func handleViewAction(_ viewAction: StepFeatureActionViewAction) {
        print("StepView :: \(#function) viewAction = \(viewAction)")
    }
}

struct StepView_Previews: PreviewProvider {
    static var previews: some View {
        StepAssembly(stepID: 4350).makeModule()
    }
}
