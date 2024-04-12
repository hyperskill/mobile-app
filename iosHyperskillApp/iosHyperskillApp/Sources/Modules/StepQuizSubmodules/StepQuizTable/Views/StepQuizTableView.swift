import SwiftUI

struct StepQuizTableView: View {
    @ObservedObject var viewModel: StepQuizTableViewModel

    private(set) var panModalPresenter: PanModalPresenter

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ForEach(viewModel.viewData.rows) { row in
                StepQuizTableRowView(
                    title: row.text,
                    subtitle: row.subtitle,
                    onTap: {
                        doSelectColumnsPresentation(for: row)
                    }
                )
            }
        }
        .padding(.bottom, LayoutInsets.smallInset)
        .opacity(isEnabled ? 1 : 0.5)
        .animation(.easeInOut(duration: 0.33), value: isEnabled)
    }

    private func doSelectColumnsPresentation(for row: StepQuizTableViewData.Row) {
        let viewController = viewModel.makeSelectColumnsViewController(for: row)
        panModalPresenter.presentPanModal(viewController)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        VStack(spacing: LayoutInsets.defaultInset) {
            StepQuizTableAssembly
                .makePlaceholder(isMultipleChoice: false)
                .makeModule()

            Divider()

            StepQuizTableAssembly
                .makePlaceholder(isMultipleChoice: false)
                .makeModule()
                .disabled(true)
        }
        .padding()
    }
}
#endif
