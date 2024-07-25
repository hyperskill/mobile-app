import SnapKit
import UIKit

protocol TableQuizSelectColumnsViewDelegate: AnyObject {
    func tableQuizSelectColumnsView(
        _ view: TableQuizSelectColumnsView,
        didSelectColumn column: StepQuizTableViewData.Column,
        isOn: Bool
    )
}

extension TableQuizSelectColumnsView {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground
    }
}

final class TableQuizSelectColumnsView: UIView {
    let appearance: Appearance

    weak var delegate: TableQuizSelectColumnsViewDelegate?

    private lazy var headerView = TableQuizSelectColumnsHeaderView()

    private lazy var columnsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        return stackView
    }()

    private lazy var scrollableContentStackView = UIKitScrollableStackView(orientation: .vertical)

    private var columns = [StepQuizTableViewData.Column]()
    private var selectedColumnsIDs = Set<Int>()

    var prompt: String? {
        didSet {
            self.headerView.prompt = self.prompt
        }
    }

    var title: String? {
        didSet {
            self.headerView.title = self.title
        }
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func set(columns: [StepQuizTableViewData.Column], selectedColumnsIDs: Set<Int>) {
        if self.columns == columns {
            return
        }

        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs

        if !self.columnsStackView.arrangedSubviews.isEmpty {
            self.columnsStackView.removeAllArrangedSubviews()
        }

        for column in columns {
            let columnView = TableQuizSelectColumnsColumnView()
            columnView.onValueChanged = { [weak self] isOn in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.delegate?.tableQuizSelectColumnsView(strongSelf, didSelectColumn: column, isOn: isOn)
            }
            columnView.tag = column.id

            self.columnsStackView.addArrangedSubview(columnView)

            columnView.setOn(self.selectedColumnsIDs.contains(column.id), animated: false)
            columnView.setTitle(column.text)
        }
    }

    func update(selectedColumnsIDs: Set<Int>) {
        self.selectedColumnsIDs = selectedColumnsIDs

        for arrangedSubview in self.columnsStackView.arrangedSubviews {
            guard let columnView = arrangedSubview as? TableQuizSelectColumnsColumnView else {
                continue
            }

            let id = columnView.tag
            let isOn = selectedColumnsIDs.contains(id)

            let animated = columnView.isOn != isOn

            columnView.setOn(isOn, animated: animated)
        }
    }
}

extension TableQuizSelectColumnsView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.scrollableContentStackView)

        self.scrollableContentStackView.addArrangedView(self.headerView)
        self.scrollableContentStackView.addArrangedView(self.columnsStackView)
    }

    func makeConstraints() {
        self.scrollableContentStackView.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.trailing.equalTo(self.safeAreaLayoutGuide)
        }
    }
}

extension TableQuizSelectColumnsView: PanModalScrollable {
    var panScrollable: UIScrollView? { self.scrollableContentStackView.panScrollable }
}
