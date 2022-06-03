import PanModal
import SwiftUI
import UIKit

final class StepQuizTableSelectColumnsViewController: PanModalPresentableViewController {
    private let row: StepQuizTableViewData.Row
    private let columns: [StepQuizTableViewData.Column]
    private var selectedColumnsIDs: Set<Int>
    private let isMultipleChoice: Bool
    private let onColumnsChanged: (Set<Int>) -> Void

    override var panScrollable: UIScrollView? { self.scrollView }

    private lazy var scrollView = UIScrollView()

    init(
        row: StepQuizTableViewData.Row,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsChanged: @escaping (Set<Int>) -> Void
    ) {
        self.row = row
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsChanged = onColumnsChanged

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
            ? Strings.StepQuizTable.singleChoicePrompt
            : Strings.StepQuizTable.multipleChoicePrompt

        let contentView = StepQuizTableSelectColumnsView(
            prompt: prompt,
            title: self.row.text,
            columns: self.columns,
            selectedColumnsIDs: self.selectedColumnsIDs,
            isMultipleChoice: self.isMultipleChoice,
            onColumnsChanged: self.onColumnsChanged
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
}
