import PanModal
import UIKit

extension StepQuizTableSelectColumnsViewController {
    enum Animation {
        static let dismissAnimationDelay: TimeInterval = 0.33
    }
}

final class StepQuizTableSelectColumnsViewController: PanModalPresentableViewController {
    private let rowTitle: String
    private let columns: [StepQuizTableViewData.Column]
    private var selectedColumnsIDs: Set<Int>
    private let isMultipleChoice: Bool
    private let onColumnsSelected: (Set<Int>) -> Void

    var tableQuizSelectColumnsView: StepQuizTableSelectColumnsView? { self.view as? StepQuizTableSelectColumnsView }

    override var panScrollable: UIScrollView? { tableQuizSelectColumnsView?.panScrollable }

    override var shortFormHeight: PanModalHeight { longFormHeight }

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
    }

    override func loadView() {
        let view = StepQuizTableSelectColumnsView(frame: UIScreen.main.bounds)
        self.view = view
        view.delegate = self
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        tableQuizSelectColumnsView?.prompt = isMultipleChoice
            ? Strings.StepQuizTable.multipleChoicePrompt
            : Strings.StepQuizTable.singleChoicePrompt
        tableQuizSelectColumnsView?.title = rowTitle
        tableQuizSelectColumnsView?.isMultipleChoice = isMultipleChoice

        tableQuizSelectColumnsView?.set(columns: columns, selectedColumnsIDs: selectedColumnsIDs)
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        DispatchQueue.main.async {
            self.panModalSetNeedsLayoutUpdate()
            self.panModalTransition(to: .longForm)
        }
    }
}

extension StepQuizTableSelectColumnsViewController: StepQuizTableSelectColumnsViewDelegate {
    func tableQuizSelectColumnsView(
        _ view: StepQuizTableSelectColumnsView,
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

    func tableQuizSelectColumnsViewDidTapConfirm(_ view: StepQuizTableSelectColumnsView) {
        onColumnsSelected(selectedColumnsIDs)

        DispatchQueue.main.asyncAfter(deadline: .now() + Animation.dismissAnimationDelay) {
            self.dismiss(animated: true)
        }
    }
}
