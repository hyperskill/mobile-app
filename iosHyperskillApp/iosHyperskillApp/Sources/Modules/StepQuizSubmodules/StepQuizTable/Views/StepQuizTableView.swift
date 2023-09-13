import SwiftUI

struct StepQuizTableView: View {
    @ObservedObject var viewModel: StepQuizTableViewModel

    private(set) var panModalPresenter: PanModalPresenter

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
    }

    private func doSelectColumnsPresentation(for row: StepQuizTableViewData.Row) {
        let viewController = viewModel.makeSelectColumnsViewController(for: row)
        panModalPresenter.presentPanModal(viewController)
    }
}

#if DEBUG
struct StepQuizTableView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizTableAssembly
            .makePlaceholder(isMultipleChoice: false)
            .makeModule()
            .previewLayout(.sizeThatFits)
    }
}
#endif
