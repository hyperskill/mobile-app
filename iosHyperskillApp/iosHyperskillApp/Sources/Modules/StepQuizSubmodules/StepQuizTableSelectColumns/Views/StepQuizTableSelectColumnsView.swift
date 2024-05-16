import SwiftUI

struct StepQuizTableSelectColumnsView: View {
    let prompt: String
    let title: String

    let columns: [StepQuizTableViewData.Column]
    @State private(set) var selectedColumnsIDs: Set<Int>

    let isMultipleChoice: Bool

    var onColumnsChanged: ((Set<Int>) -> Void)?
    var onConfirmTapped: (() -> Void)?

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            Text(prompt)
                .font(.caption)
                .foregroundColor(.primaryText)

            VStack(alignment: .leading, spacing: 0) {
                LatexView(
                    text: title,
                    configuration: .quizContent()
                )

                VStack(alignment: .leading, spacing: 0) {
                    ForEach(columns) { column in
                        StepQuizTableSelectColumnsColumnView(
                            isSelected: selectedColumnsIDs.contains(column.id),
                            text: column.text,
                            isMultipleChoice: isMultipleChoice,
                            onTap: {
                                handleColumnTapped(column: column)
                            }
                        )
                        .padding(.vertical, LayoutInsets.smallInset)
                    }
                }
                .padding(.bottom, LayoutInsets.smallInset)

                Button(Strings.StepQuizTable.confirmButton, action: { onConfirmTapped?() })
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                    .padding(.vertical)

                Spacer()
            }
        }
        .padding()
        .ignoresSafeArea()
    }

    private func handleColumnTapped(column: StepQuizTableViewData.Column) {
        let isContains = selectedColumnsIDs.contains(column.id)

        if isMultipleChoice {
            if isContains {
                selectedColumnsIDs.remove(column.id)
            } else {
                selectedColumnsIDs.insert(column.id)
            }
        } else {
            assert(selectedColumnsIDs.count <= 1, "Sigle choice")
            selectedColumnsIDs.removeAll()

            if !isContains {
                selectedColumnsIDs.insert(column.id)
            }
        }

        onColumnsChanged?(selectedColumnsIDs)
    }
}

struct StepQuizTableSelectColumnsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizTableSelectColumnsView(
                prompt: "Choose from the table",
                title: "Variant A",
                columns: [
                    .init(text: "1"),
                    .init(text: "2"),
                    .init(text: "3")
                ],
                selectedColumnsIDs: ["2".hashValue],
                isMultipleChoice: false,
                onColumnsChanged: { _ in },
                onConfirmTapped: {}
            )

            StepQuizTableSelectColumnsView(
                prompt: "Choose one or multiple options",
                title: "Variant A",
                columns: [.init(text: "1"), .init(text: "2"), .init(text: "3")],
                selectedColumnsIDs: ["1".hashValue, "2".hashValue],
                isMultipleChoice: true,
                onColumnsChanged: { _ in },
                onConfirmTapped: {}
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
