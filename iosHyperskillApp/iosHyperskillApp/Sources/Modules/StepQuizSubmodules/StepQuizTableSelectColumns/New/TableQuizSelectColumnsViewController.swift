import PanModal
import UIKit

protocol TableQuizSelectColumnsViewControllerDelegate: AnyObject {
    func tableQuizSelectColumnsViewController(
        _ controller: TableQuizSelectColumnsViewController,
        didSelectColumn column: StepQuizTableViewData.Column
    )
}

final class TableQuizSelectColumnsViewController: PanModalPresentableViewController {
    private let rowTitle: String
    private let columns: [StepQuizTableViewData.Column]
    private var selectedColumnsIDs: Set<Int>
    private let isMultipleChoice: Bool
    private let onColumnsSelected: (Set<Int>) -> Void

    weak var delegate: TableQuizSelectColumnsViewControllerDelegate?

    var tableQuizSelectColumnsView: TableQuizSelectColumnsView? { self.view as? TableQuizSelectColumnsView }

    override var panScrollable: UIScrollView? { tableQuizSelectColumnsView?.panScrollable }

    init(
        title: String,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>,
        isMultipleChoice: Bool,
        onColumnsSelected: @escaping (Set<Int>) -> Void
    ) {
        self.rowTitle = title
        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs
        self.isMultipleChoice = isMultipleChoice
        self.onColumnsSelected = onColumnsSelected

        super.init()

        self.isShortFormEnabled = false
    }

    override func loadView() {
        let view = TableQuizSelectColumnsView(frame: UIScreen.main.bounds)
        self.view = view
        view.delegate = self
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        tableQuizSelectColumnsView?.prompt = isMultipleChoice
            ? Strings.StepQuizTable.multipleChoicePrompt
            : Strings.StepQuizTable.singleChoicePrompt
        tableQuizSelectColumnsView?.title = rowTitle
        tableQuizSelectColumnsView?.set(columns: columns, selectedColumnsIDs: selectedColumnsIDs)

        panModalSetNeedsLayoutUpdate()
    }

    override func shouldTransition(to state: PanModalPresentationController.PresentationState) -> Bool {
        switch state {
        case .shortForm:
            false
        case .longForm:
            true
        }
    }
}

extension TableQuizSelectColumnsViewController: TableQuizSelectColumnsViewDelegate {
    func tableQuizSelectColumnsView(
        _ view: TableQuizSelectColumnsView,
        didSelectColumn column: StepQuizTableViewData.Column,
        isOn: Bool
    ) {
        if isMultipleChoice {
            if isOn {
                selectedColumnsIDs.insert(column.id)
            } else {
                selectedColumnsIDs.remove(column.id)
            }
        } else {
            assert(selectedColumnsIDs.count <= 1, "Sigle choice")
            selectedColumnsIDs.removeAll()

            if isOn {
                selectedColumnsIDs.insert(column.id)
            }
        }

        tableQuizSelectColumnsView?.update(selectedColumnsIDs: selectedColumnsIDs)
    }
}
