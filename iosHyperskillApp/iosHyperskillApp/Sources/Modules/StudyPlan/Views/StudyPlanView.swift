import shared
import SwiftUI

extension StudyPlanView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset
        let spacingBetweenRelativeItems = LayoutInsets.defaultInset

        let progressBlockSpacing = LayoutInsets.smallInset
        let progressBlockTitleInsets = LayoutInsets(bottom: LayoutInsets.smallInset)

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct StudyPlanView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StudyPlanViewModel

    @StateObject var stackRouter: SwiftUIStackRouter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationTitle(Strings.StudyPlan.title)
        .navigationViewStyle(StackNavigationViewStyle())
        .toolbar {
            GamificationToolbarContent(
                stateKs: viewModel.gamificationToolbarStateKs,
                onGemsTap: viewModel.doGemsBarButtonItemAction,
                onStreakTap: viewModel.doStreakBarButtonItemAction
            )
        }
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
        switch viewModel.studyPlanWidgetStateKs {
        case .idle:
            TrackSkeletonView()
                .onAppear {
                    viewModel.doLoadStudyPlan()
                }
        case .loading:
            TrackSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryContentLoading
                )
            )
        case .content(let data):
            ScrollView {
                if let trackTitle = viewModel.state.trackTitle {
                    Text(trackTitle)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)
                }

                ForEach(data.sections, id: \.id) { section in
                    StudyPlanSectionView(section: section)
                }
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func handleViewAction(_ viewAction: StudyPlanScreenFeatureActionViewAction) {
        print("View action")
    }
}

struct StudyPlanView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StudyPlanAssembly().makeModule()
        }
    }
}
