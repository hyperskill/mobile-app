import SwiftUI

struct StepQuizMatchingView: View {
    @ObservedObject var viewModel: StepQuizMatchingViewModel

    @EnvironmentObject private var panModalPresenter: PanModalPresenter

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ForEach(viewModel.viewData.items) { item in
                StepQuizTableRowView(
                    title: item.title.text,
                    subtitle: item.option.text,
                    onTap: { doSelectColumnsPresentation(for: item) }
                )
            }
        }
        .conditionalOpacity(isEnabled: isEnabled)
    }

    private func doSelectColumnsPresentation(for matchItem: StepQuizMatchingViewData.MatchItem) {
        let viewController = viewModel.makeSelectColumnsViewController(for: matchItem)
        panModalPresenter.presentPanModal(viewController)
    }
}

#if DEBUG
#Preview {
    StepQuizMatchingAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
}

#Preview("Disabled") {
    StepQuizMatchingAssembly
        .makePlaceholder()
        .makeModule()
        .padding()
        .disabled(true)
}
#endif
