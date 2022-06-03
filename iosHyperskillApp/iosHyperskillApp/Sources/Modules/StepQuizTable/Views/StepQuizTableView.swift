import SwiftUI

struct StepQuizTableView: View {
    @State var viewData: StepQuizTableViewData

    @EnvironmentObject private var panModalPresenter: PanModalPresenter

    var body: some View {
        VStack(spacing: 0) {
            ForEach(viewData.rows) { row in
                StepQuizTableRowView(
                    title: row.text,
                    subtitle: makeFormattedSubtitleText(row: row),
                    onTap: {
                        doSelectColumnsPresentation(for: row)
                    }
                )
                .padding()
            }
        }
    }

    private func makeFormattedSubtitleText(row: StepQuizTableViewData.Row) -> String {
        row.answers.map(\.text).joined(separator: ", ")
    }

    private func doSelectColumnsPresentation(for row: StepQuizTableViewData.Row) {
        let viewController = StepQuizTableSelectColumnsViewController(
            row: row,
            columns: viewData.columns,
            selectedColumnsIDs: Set(row.answers.map(\.id)),
            isMultipleChoice: viewData.isMultipleChoice,
            onColumnsChanged: { selectedColumnsIDs in
                guard let targetRowIndex = viewData.rows.firstIndex(where: { $0.id == row.id }) else {
                    return
                }

                viewData.rows[targetRowIndex].answers = viewData.columns.filter { selectedColumnsIDs.contains($0.id) }
            }
        )

        panModalPresenter.presentPanModal(viewController)
    }
}

#if DEBUG
struct StepQuizTableView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizTableView(viewData: .singleChoicePlaceholder)
            .environmentObject(PanModalPresenter(sourcelessRouter: SourcelessRouter()))
            .previewLayout(.sizeThatFits)
    }
}
#endif
