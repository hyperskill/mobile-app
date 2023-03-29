import shared
import SwiftUI

extension ProblemsLimitView {
    struct Appearance {
        var showTopDivider = false

        let skeletonHeight: CGFloat = 40
    }
}

struct ProblemsLimitView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProblemsLimitViewModel

    private var skeleton: some View {
        SkeletonRoundedView()
            .frame(height: appearance.skeletonHeight)
    }

    var body: some View {
        buildBody()
            .onAppear(perform: viewModel.startListening)
            .onDisappear(perform: viewModel.stopListening)
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
                if appearance.showTopDivider {
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
}

struct ProblemsLimitView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitAssembly()
            .makeModule()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
