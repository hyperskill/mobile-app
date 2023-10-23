import SnapKit
import UIKit

extension FillBlanksInputCollectionViewCell {
    struct Appearance {
        let minWidth: CGFloat = 48

        let cornerRadius: CGFloat = 8

        let insets = LayoutInsets.small.uiEdgeInsets

        static let font = CodeEditorThemeService().theme.font
        let textColor = UIColor.primaryText
    }
}

final class FillBlanksInputCollectionViewCell: UICollectionViewCell, Reusable {
    var appearance = Appearance()

    private lazy var inputContainerView: FillBlanksQuizInputContainerView = {
        let view = FillBlanksQuizInputContainerView(
            appearance: .init(cornerRadius: self.appearance.cornerRadius)
        )
        return view
    }()

    private lazy var textField: UITextField = {
        let textField = UITextField()
        textField.font = Appearance.font
        textField.textColor = self.appearance.textColor
        textField.textAlignment = .center
        textField.delegate = self
        textField.addTarget(self, action: #selector(self.textFieldDidChange(_:)), for: .editingChanged)
        // Disable features
        textField.autocapitalizationType = .none
        textField.autocorrectionType = .no
        textField.spellCheckingType = .no
        textField.smartDashesType = .no
        textField.smartQuotesType = .no
        textField.smartInsertDeleteType = .no
        return textField
    }()

    var text: String? {
        didSet {
            self.textField.text = self.text
        }
    }

    var isEnabled = true {
        didSet {
            self.isUserInteractionEnabled = self.isEnabled
        }
    }

    var state: FillBlanksQuizInputContainerView.State {
        get {
            self.inputContainerView.state
        }
        set {
            self.inputContainerView.state = newValue
        }
    }

    var onInputChanged: ((String) -> Void)?

    var onBecameFirstResponder: (() -> Void)?
    var onResignedFirstResponder: (() -> Void)?

    override init(frame: CGRect) {
        super.init(frame: frame)

        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func becomeFirstResponder() -> Bool {
        self.textField.becomeFirstResponder()
    }

    @objc
    private func textFieldDidChange(_ sender: UITextField) {
        self.onInputChanged?(sender.text ?? "")
    }

    static func calculatePreferredContentSize(text: String, maxWidth: CGFloat) -> CGSize {
        let appearance = Appearance()

        let sizeOfString = Appearance.font.sizeOfString(string: text, constrainedToWidth: Double(maxWidth))
        let widthOfStringWithInsets = appearance.insets.left + sizeOfString.width.rounded(.up) + appearance.insets.right

        let width = max(appearance.minWidth, min(maxWidth, widthOfStringWithInsets))
        let height = (appearance.insets.top + Appearance.font.pointSize + appearance.insets.bottom).rounded(.up)

        return CGSize(width: width, height: height)
    }
}

extension FillBlanksInputCollectionViewCell: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        self.contentView.addSubview(self.inputContainerView)
        self.inputContainerView.addSubview(self.textField)
    }

    func makeConstraints() {
        self.inputContainerView.translatesAutoresizingMaskIntoConstraints = false
        self.inputContainerView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        self.textField.translatesAutoresizingMaskIntoConstraints = false
        self.textField.snp.makeConstraints { make in
            make.edges.equalToSuperview().inset(self.appearance.insets)
        }
    }
}

// MARK: - FillBlanksInputCollectionViewCell: UITextFieldDelegate -

extension FillBlanksInputCollectionViewCell: UITextFieldDelegate {
    func textFieldDidBeginEditing(_ textField: UITextField) {
        onBecameFirstResponder?()
    }

    func textFieldDidEndEditing(_ textField: UITextField) {
        onResignedFirstResponder?()
    }
}
