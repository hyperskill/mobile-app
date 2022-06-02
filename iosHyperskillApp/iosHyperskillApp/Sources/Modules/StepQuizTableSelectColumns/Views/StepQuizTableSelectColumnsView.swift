import SwiftUI

struct StepQuizTableSelectColumnsView: View {
    private let prompt: String
    private let title: String

    private let columns: [StepQuizTableViewData.Column]
    @State private var selectedColumnsIDs: Set<Int>

    private let isMultipleChoice: Bool

    private let onColumnsChanged: (Set<Int>) -> Void

    init(
        prompt: String,
        title: String,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsChanged: @escaping (Set<Int>) -> Void
    ) {
        self.prompt = prompt
        self.title = title
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsChanged = onColumnsChanged
    }

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            Text(prompt)
                .font(.caption)
                .foregroundColor(.primaryText)

            VStack(alignment: .stepQuizTableSelectColumnsTitleAlignmentGuide, spacing: LayoutInsets.defaultInset) {
                Text(title)
                    .font(.body)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.leading)
                    .alignmentGuide(.stepQuizTableSelectColumnsTitleAlignmentGuide) { context in
                        context[.leading]
                    }

                VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                    ForEach(columns) { column in
                        StepQuizTableSelectColumnsColumnView(
                            isSelected: .constant(selectedColumnsIDs.contains(column.id)),
                            text: column.text,
                            isMultipleChoice: isMultipleChoice,
                            onTap: {
                                handleColumnTapped(column: column)
                            }
                        )
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                }
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

extension HorizontalAlignment {
    /// A custom alignment for title and text.
    private struct StepQuizTableSelectColumnsTitleAlignment: AlignmentID {
        static func defaultValue(in context: ViewDimensions) -> CGFloat {
            context[HorizontalAlignment.leading]
        }
    }

    /// A guide for aligning titles.
    static let stepQuizTableSelectColumnsTitleAlignmentGuide = HorizontalAlignment(
        StepQuizTableSelectColumnsTitleAlignment.self
    )
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
                onColumnsChanged: { _ in }
            )

            StepQuizTableSelectColumnsView(
                prompt: "Choose one or multiple options",
                title: "Variant A",
                columns: [.init(text: "1"), .init(text: "2"), .init(text: "3")],
                selectedColumnsIDs: ["1".hashValue, "2".hashValue],
                isMultipleChoice: true,
                onColumnsChanged: { _ in }
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
