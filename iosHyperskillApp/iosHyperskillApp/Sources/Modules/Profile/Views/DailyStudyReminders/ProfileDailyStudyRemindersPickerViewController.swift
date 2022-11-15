import PanModal
import SnapKit
import UIKit

extension ProfileDailyStudyRemindersPickerViewController {
    struct Appearance {
        let pickerViewHeight: CGFloat = 216
        let pickerViewInsets = LayoutInsets(horizontal: LayoutInsets.smallInset, vertical: LayoutInsets.defaultInset)

        let confirmButtonHeight: CGFloat = 44
        let confirmButtonInsets = LayoutInsets.default

        let backgroundColor = UIColor.systemBackground
    }
}

final class ProfileDailyStudyRemindersPickerViewController: PanModalPresentableViewController {
    private let appearance: Appearance

    private let rows: [String]
    private let initialRowIndex: Int

    private weak var pickerView: UIPickerView?

    override var shortFormHeight: PanModalHeight {
        let pickerViewHeightWithInsets = appearance.pickerViewInsets.top + appearance.pickerViewHeight
        let confirmButtonHeightWithInsets = appearance.confirmButtonInsets.top
            + appearance.confirmButtonHeight
            + appearance.confirmButtonInsets.bottom

        let height = pickerViewHeightWithInsets + confirmButtonHeightWithInsets

        return .contentHeight(height)
    }

    // Disables transition to longForm when dragging up
    override var longFormHeight: PanModalHeight { shortFormHeight }

    var onDidConfirmRow: ((Int) -> Void)?

    init(
        rows: [String],
        initialRowIndex: Int,
        onDidConfirmRow: ((Int) -> Void)?,
        appearance: Appearance = Appearance()
    ) {
        self.rows = rows
        self.initialRowIndex = initialRowIndex
        self.onDidConfirmRow = onDidConfirmRow
        self.appearance = appearance

        super.init()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        pickerView?.selectRow(initialRowIndex, inComponent: 0, animated: false)
    }

    // MARK: Private API

    private func setup() {
        view.backgroundColor = appearance.backgroundColor

        setupPickerView()
        setupConfirmButton()
    }

    private func setupPickerView() {
        let pickerView = UIPickerView()
        pickerView.dataSource = self
        pickerView.delegate = self

        view.addSubview(pickerView)
        pickerView.translatesAutoresizingMaskIntoConstraints = false
        pickerView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.pickerViewInsets.top)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.pickerViewInsets.leading)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.pickerViewInsets.trailing)
            make.height.equalTo(appearance.pickerViewHeight)
        }

        self.pickerView = pickerView
    }

    private func setupConfirmButton() {
        let confirmButton = UIKitRoundedRectangleButton(style: .violet)
        confirmButton.addTarget(self, action: #selector(confirmButtonTapped), for: .touchUpInside)

        view.addSubview(confirmButton)
        confirmButton.translatesAutoresizingMaskIntoConstraints = false
        confirmButton.snp.makeConstraints { make in
            make.top.equalTo(pickerView.require().snp.bottom).offset(appearance.confirmButtonInsets.top)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(appearance.confirmButtonInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.confirmButtonInsets.bottom)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-appearance.confirmButtonInsets.trailing)
            make.height.equalTo(appearance.confirmButtonHeight)
        }

        confirmButton.setTitle(Strings.StepQuizTable.confirmButton, for: .normal)
    }

    @objc
    private func confirmButtonTapped() {
        guard let selectedRowIndex = pickerView?.selectedRow(inComponent: 0) else {
            return
        }

        onDidConfirmRow?(selectedRowIndex)
    }
}

// MARK: - ProfileDailyStudyRemindersPickerViewController: UIPickerViewDataSource -

extension ProfileDailyStudyRemindersPickerViewController: UIPickerViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int { 1 }

    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int { rows.count }
}

// MARK: - ProfileDailyStudyRemindersPickerViewController: UIPickerViewDelegate -

extension ProfileDailyStudyRemindersPickerViewController: UIPickerViewDelegate {
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        rows[row]
    }
}
