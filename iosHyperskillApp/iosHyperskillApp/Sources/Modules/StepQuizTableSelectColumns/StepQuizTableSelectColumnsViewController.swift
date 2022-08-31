import PanModal
import SwiftUI
import UIKit

extension StepQuizTableSelectColumnsViewController {
    enum Animation {
        static let dismissAnimationDelay: TimeInterval = 0.33
    }
}

final class StepQuizTableSelectColumnsViewController: PanModalPresentableViewController {
    private let row: StepQuizTableViewData.Row
    private let columns: [StepQuizTableViewData.Column]
    private var selectedColumnsIDs: Set<Int>
    private let isMultipleChoice: Bool
    private let onColumnsSelected: (Set<Int>) -> Void

    override var panScrollable: UIScrollView? { self.scrollView }

    private lazy var scrollView = UIScrollView()

    init(
        row: StepQuizTableViewData.Row,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsSelected: @escaping (Set<Int>) -> Void
    ) {
        self.row = row
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsSelected = onColumnsSelected

        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.backgroundColor = .systemBackground

        self.view.addSubview(self.scrollView)
        self.scrollView.translatesAutoresizingMaskIntoConstraints = false
        self.scrollView.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.trailing.equalTo(self.view.safeAreaLayoutGuide)
            make.width.equalToSuperview()
        }

        let prompt = self.isMultipleChoice
            ? Strings.StepQuizTable.multipleChoicePrompt
            : Strings.StepQuizTable.singleChoicePrompt

        let contentView = StepQuizTableSelectColumnsView(
            prompt: prompt,
            title: self.row.text,
            columns: self.columns,
            selectedColumnsIDs: self.selectedColumnsIDs,
            isMultipleChoice: self.isMultipleChoice,
            onColumnsChanged: self.handleColumnsChanged(_:),
            onConfirmTapped: self.finishColumnsSelection
        )
        let contentHostingController = UIHostingController(rootView: contentView)

        self.addChild(contentHostingController)
        self.scrollView.addSubview(contentHostingController.view)

        contentHostingController.view.translatesAutoresizingMaskIntoConstraints = false
        contentHostingController.view.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.width.equalToSuperview()
        }
        contentHostingController.didMove(toParent: self)

        self.panModalSetNeedsLayoutUpdate()
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
