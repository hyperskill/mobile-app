import shared
import SwiftUI

extension ProblemsLimitView {
    struct Appearance {
        let stackSpacing: CGFloat = 8

        let limitCircleWidthHeight: CGFloat = 8
        let limitCirclesSpacing: CGFloat = 4

        let skeletonHeight: CGFloat = 20
    }
}

struct ProblemsLimitView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProblemsLimitViewModel

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
                    viewModel.loadLimits()
                }
        case .loading:
            skeleton
        case .error:
            Button(Strings.Placeholder.networkErrorButtonText) {
                viewModel.loadLimits(forceUpdate: true)
            }
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        case .content(let content):
            switch ProblemsLimitFeatureViewStateContentKs(content) {
            case .empty:
                EmptyView()
            case .widget(let data):
                buildWidget(data: data)
            }
        }
    }

    @ViewBuilder
    private func buildWidget(data: ProblemsLimitFeatureViewStateContentWidget) -> some View {
        HStack(spacing: appearance.stackSpacing) {
            HStack(spacing: appearance.limitCirclesSpacing) {
                ForEach(0..<data.stepsLimitTotal, id: \.self) { index in
                    Circle()
                        .foregroundColor(
                            index < data.stepsLimitLeft
                            ? Color(ColorPalette.overlayViolet)
                            : Color(ColorPalette.onSurfaceAlpha12)
                        )
                        .frame(widthHeight: appearance.limitCircleWidthHeight)
                }

                Text(data.stepsLimitLabel)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)

                Text(data.updateInLabel)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
            }
        }
    }

    private func handleViewAction(_ viewAction: ProblemsLimitFeatureActionViewAction) {
        print("ProblemsLimitFeatureActionViewAction :: \(viewAction)")
    }

    private var skeleton: some View {
        SkeletonRoundedView()
            .frame(height: appearance.skeletonHeight)
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
