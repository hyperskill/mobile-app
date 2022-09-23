import UIKit

extension AuthTextField {
    enum Appearance {
        static let tintColor = UIColor.placeholderText

        static let eyeButtonSize = CGSize(width: 24, height: 24)
        static let eyeButtonInsets = LayoutInsets(top: 0, leading: 8, bottom: 0, trailing: 4)
        static let imageEyeOpened = UIImage(systemName: "eye.fill")
        static let imageEyeClosed = UIImage(systemName: "eye.slash.fill")
    }
}

final class AuthTextField: UITextField {
    private lazy var eyeButton: UIButton = {
        let button = UIButton(type: .system)
        button.setImage(Appearance.imageEyeOpened, for: .normal)
        button.imageView?.contentMode = .scaleAspectFit
        button.tintColor = Appearance.tintColor
        button.imageEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        button.frame = CGRect(
            x: self.frame.size.width - Appearance.eyeButtonSize.width,
            y: (self.frame.size.height - Appearance.eyeButtonSize.height) / 2,
            width: Appearance.eyeButtonSize.width,
            height: Appearance.eyeButtonSize.height
        )
        button.addTarget(self, action: #selector(self.togglePasswordField), for: .touchUpInside)
        return button
    }()

    var fieldType: InputType = .text {
        didSet {
            self.handleFieldTypeUpdated()
        }
    }

    init(frame: CGRect = .zero, type: InputType) {
        super.init(frame: frame)

        self.fieldType = type
        self.handleFieldTypeUpdated()
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func textRect(forBounds bounds: CGRect) -> CGRect { self.contentRect(for: bounds) }

    override func editingRect(forBounds bounds: CGRect) -> CGRect { self.contentRect(for: bounds) }

    override func rightViewRect(forBounds bounds: CGRect) -> CGRect {
        if self.fieldType == .text {
            return .zero
        } else {
            return CGRect(
                x: bounds.width - Appearance.eyeButtonSize.width - Appearance.eyeButtonInsets.trailing,
                y: (bounds.size.height - Appearance.eyeButtonSize.height) / 2,
                width: Appearance.eyeButtonSize.width,
                height: Appearance.eyeButtonSize.height
            )
        }
    }

    private func handleFieldTypeUpdated() {
        switch self.fieldType {
        case .password:
            self.rightView = self.eyeButton
            self.rightViewMode = .always
        default:
            rightView = nil
        }
    }

    @objc
    private func togglePasswordField(_ sender: Any) {
        self.isSecureTextEntry.toggle()

        if let button = self.rightView as? UIButton {
            button.setImage(
                self.isSecureTextEntry ? Appearance.imageEyeOpened : Appearance.imageEyeClosed,
                for: .normal
            )
        }
    }

    private func contentRect(for bounds: CGRect) -> CGRect {
        if self.fieldType == .text {
            return bounds
        } else {
            return CGRect(
                x: bounds.origin.x,
                y: bounds.origin.y,
                width: bounds.width - Appearance.eyeButtonSize.width - Appearance.eyeButtonInsets.leading,
                height: bounds.height
            )
        }
    }

    enum InputType {
        case text
        case password
    }
}
