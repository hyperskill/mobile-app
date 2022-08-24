import SwiftUI

extension StepQuizTableSelectColumnsView {
    struct Appearance {
        let titleLeadingInset: CGFloat = 28
        let titleLeadingInsetMultipleChoice: CGFloat = 26
    }
}

struct StepQuizTableSelectColumnsView: View {
    private(set) var appearance = Appearance()

    private let prompt: String
    private let title: String

    private let columns: [StepQuizTableViewData.Column]
    @State private var selectedColumnsIDs: Set<Int>

    private let isMultipleChoice: Bool

    private let onColumnsChanged: (Set<Int>) -> Void

    private let onConfirmTapped: () -> Void

    init(
        prompt: String,
        title: String,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsChanged: @escaping (Set<Int>) -> Void,
        onConfirmTapped: @escaping () -> Void
    ) {
        self.prompt = prompt
        self.title = title
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsChanged = onColumnsChanged
        self.onConfirmTapped = onConfirmTapped
    }

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            Text(prompt)
                .font(.caption)
                .foregroundColor(.primaryText)

            VStack(alignment: .leading, spacing: 0) {
                LatexView(
                    text: .constant(title),
                    configuration: .quizContent()
                )
                .padding(
                    .leading,
                    isMultipleChoice ? appearance.titleLeadingInsetMultipleChoice : appearance.titleLeadingInset
                )
                .padding(.bottom, LayoutInsets.smallInset)

                VStack(alignment: .leading, spacing: 0) {
                    ForEach(columns) { column in
                        StepQuizTableSelectColumnsColumnView(
                            isSelected: .constant(selectedColumnsIDs.contains(column.id)),
                            text: column.text,
                            isMultipleChoice: isMultipleChoice,
                            onTap: {
                                handleColumnTapped(column: column)
                            }
                        )
                    }
                }

                Button(Strings.StepQuizTable.confirmButton, action: onConfirmTapped)
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

        onColumnsChanged(selectedColumnsIDs)
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
