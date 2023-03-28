import shared
import SwiftUI

extension ProblemsLimitView {
    struct Appearance {
        let skeletonHeight: CGFloat = 40
    }
}

struct ProblemsLimitView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProblemsLimitViewModel

    let showDivider: Bool

    private var skeleton: some View {
        SkeletonRoundedView()
            .frame(height: appearance.skeletonHeight)
    }

    var body: some View {
        buildBody()
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)
            }
            .onDisappear {
                viewModel.stopListening()
                viewModel.onViewAction = nil
            }
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            skeleton
                .onAppear {
                    viewModel.doLoadLimits()
                }
        case .loading:
            skeleton
        case .error:
            Button(Strings.Placeholder.networkErrorButtonText) {
                viewModel.doLoadLimits(forceUpdate: true)
            }
            .buttonStyle(OutlineButtonStyle())
        case .content(let content):
            switch ProblemsLimitFeatureViewStateContentKs(content) {
            case .empty:
                EmptyView()
            case .widget(let data):
                if showDivider {
                    Divider()
                }

                ProblemsLimitWidgetView(
                    stepsLimitLeft: Int(data.stepsLimitLeft),
                    stepsLimitTotal: Int(data.stepsLimitTotal),
                    stepsLimitLabel: data.stepsLimitLabel,
                    updateInLabel: data.updateInLabel
                )
            }
        }
    }

    private func handleViewAction(_ viewAction: ProblemsLimitFeatureActionViewAction) {
        print("ProblemsLimitFeatureActionViewAction :: \(viewAction)")
    }
}

struct ProblemsLimitView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitAssembly(showDivider: false)
            .makeModule()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
