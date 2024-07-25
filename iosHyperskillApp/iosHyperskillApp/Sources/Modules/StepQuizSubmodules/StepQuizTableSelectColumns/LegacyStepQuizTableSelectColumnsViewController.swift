import PanModal
import SwiftUI
import UIKit

extension LegacyStepQuizTableSelectColumnsViewController {
    enum Animation {
        static let dismissAnimationDelay: TimeInterval = 0.33
    }
}

final class LegacyStepQuizTableSelectColumnsViewController: PanModalSwiftUIViewController<LegacyStepQuizTableSelectColumnsView> {
    private let columns: [StepQuizTableViewData.Column]
    private var selectedColumnsIDs: Set<Int>
    private let isMultipleChoice: Bool
    private let onColumnsSelected: (Set<Int>) -> Void

    init(
        title: String,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsSelected: @escaping (Set<Int>) -> Void
    ) {
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsSelected = onColumnsSelected

        let prompt = isMultipleChoice
            ? Strings.StepQuizTable.multipleChoicePrompt
            : Strings.StepQuizTable.singleChoicePrompt

        var view = LegacyStepQuizTableSelectColumnsView(
            prompt: prompt,
            title: title,
            columns: columns,
            selectedColumnsIDs: selectedColumnsIDs,
            isMultipleChoice: isMultipleChoice
        )

        super.init(
            isPresented: .constant(false),
            content: { view }
        )

        view.onColumnsChanged = self.handleColumnsChanged(_:)
        view.onConfirmTapped = self.finishColumnsSelection
    }

    // MARK: Private API

    private func handleColumnsChanged(_ newColumns: Set<Int>) {
        self.selectedColumnsIDs = newColumns
    }

    private func finishColumnsSelection() {
        self.onColumnsSelected(self.selectedColumnsIDs)

        DispatchQueue.main.asyncAfter(deadline: .now() + Animation.dismissAnimationDelay) {
            self.dismiss(animated: true)
        }
    }
}
