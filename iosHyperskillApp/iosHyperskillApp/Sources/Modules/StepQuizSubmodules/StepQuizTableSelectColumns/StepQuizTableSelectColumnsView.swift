import SnapKit
import UIKit

protocol StepQuizTableSelectColumnsViewDelegate: AnyObject {
    func tableQuizSelectColumnsView(
        _ view: StepQuizTableSelectColumnsView,
        didSelectColumn column: StepQuizTableViewData.Column,
        isOn: Bool
    )
    func tableQuizSelectColumnsViewDidTapConfirm(_ view: StepQuizTableSelectColumnsView)
    func tableQuizSelectColumnsViewDidLoadContent(_ view: StepQuizTableSelectColumnsView)
}

extension StepQuizTableSelectColumnsView {
    struct Appearance {
        let backgroundColor = UIColor.systemBackground

        let confirmButtonInsets = LayoutInsets.default.uiEdgeInsets
    }
}

final class StepQuizTableSelectColumnsView: UIView {
    let appearance: Appearance

    weak var delegate: StepQuizTableSelectColumnsViewDelegate?

    private lazy var headerView = StepQuizTableSelectColumnsHeaderView()

    private lazy var columnsStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        return stackView
    }()

    private lazy var confirmButton: UIButton = {
        let button = UIKitRoundedRectangleButton(style: .violet)
        button.addTarget(self, action: #selector(confirmButtonTapped), for: .touchUpInside)
        button.setTitle(Strings.StepQuizTable.confirmButton, for: .normal)
        return button
    }()
    private lazy var confirmButtonContainerView = UIView()

    private lazy var scrollableContentStackView = UIKitScrollableStackView(orientation: .vertical)

    private var loadGroup: DispatchGroup?

    private var columns = [StepQuizTableViewData.Column]()
    private var selectedColumnsIDs = Set<Int>()

    var prompt: String? {
        didSet {
            headerView.prompt = prompt
        }
    }

    var isMultipleChoice = false

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

    func set(
        title: String,
        columns: [StepQuizTableViewData.Column],
        selectedColumnsIDs: Set<Int>
    ) {
        loadGroup = DispatchGroup()
        let enterCount = columns.count + 1 // title + columns
        for _ in 0..<enterCount {
            loadGroup?.enter()
        }
        loadGroup?.notify(queue: .main) { [weak self] in
            // dispatch_group_leave call isn't balanced with dispatch_group_enter, deinit dispatch_group_t here to
            // prevent possible future call to leave onContentLoad.
            guard let strongSelf = self else {
                return
            }

            strongSelf.loadGroup = nil
            strongSelf.delegate?.tableQuizSelectColumnsViewDidLoadContent(strongSelf)
        }

        headerView.onContentLoad = { [weak self] in
            self?.loadGroup?.leave()
        }
        headerView.title = title

        self.columns = columns
        self.selectedColumnsIDs = selectedColumnsIDs

        if !columnsStackView.arrangedSubviews.isEmpty {
            columnsStackView.removeAllArrangedSubviews()
        }

        for column in columns {
            let columnView = StepQuizTableSelectColumnsColumnView(
                appearance: .init(checkBoxBoxType: isMultipleChoice ? .square : .circle)
            )
            columnView.onValueChanged = { [weak self] isOn in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.delegate?.tableQuizSelectColumnsView(strongSelf, didSelectColumn: column, isOn: isOn)
            }
            columnView.tag = column.id
            columnView.onContentLoad = { [weak self] in
                self?.loadGroup?.leave()
            }

            columnsStackView.addArrangedSubview(columnView)

            columnView.setOn(selectedColumnsIDs.contains(column.id), animated: false)
            columnView.setTitle(column.text)
        }
    }

    func update(selectedColumnsIDs: Set<Int>) {
        self.selectedColumnsIDs = selectedColumnsIDs

        for arrangedSubview in columnsStackView.arrangedSubviews {
            guard let columnView = arrangedSubview as? StepQuizTableSelectColumnsColumnView else {
                continue
            }

            let id = columnView.tag
            let isOn = selectedColumnsIDs.contains(id)

            let animated = columnView.isOn != isOn

            columnView.setOn(isOn, animated: animated)
        }
    }

    @objc
    private func confirmButtonTapped() {
        delegate?.tableQuizSelectColumnsViewDidTapConfirm(self)
    }
}

extension StepQuizTableSelectColumnsView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(scrollableContentStackView)

        scrollableContentStackView.addArrangedView(headerView)
        scrollableContentStackView.addArrangedView(columnsStackView)
        scrollableContentStackView.addArrangedView(confirmButtonContainerView)

        confirmButtonContainerView.addSubview(confirmButton)
    }

    func makeConstraints() {
        scrollableContentStackView.translatesAutoresizingMaskIntoConstraints = false
        scrollableContentStackView.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.trailing.equalTo(safeAreaLayoutGuide)
        }

        confirmButton.translatesAutoresizingMaskIntoConstraints = false
        confirmButton.snp.makeConstraints { make in
            make.edges.equalToSuperview().inset(appearance.confirmButtonInsets)
        }
    }
}

extension StepQuizTableSelectColumnsView: PanModalScrollable {
    var panScrollable: UIScrollView? { scrollableContentStackView.panScrollable }
}
