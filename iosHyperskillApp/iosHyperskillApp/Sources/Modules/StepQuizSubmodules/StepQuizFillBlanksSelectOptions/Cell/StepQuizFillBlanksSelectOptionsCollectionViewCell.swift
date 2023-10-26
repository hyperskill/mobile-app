import SnapKit
import UIKit

extension StepQuizFillBlanksSelectOptionsCollectionViewCell {
    struct Appearance {
        static let defaultSize = CGSize(width: 48, height: 32)

        static var defaultWidth: CGFloat { Self.defaultSize.width }
        static var defaultHeight: CGFloat { Self.defaultSize.height }

        let cornerRadius: CGFloat = 8
        let insets = LayoutInsets.small

        let textColor = UIColor.primaryText
        static let font = CodeEditorThemeService().theme.font
    }
}

final class StepQuizFillBlanksSelectOptionsCollectionViewCell: UICollectionViewCell, Reusable {
    private static var prototypeTextLabel: UILabel?

    var appearance = Appearance()

    private lazy var inputContainerView: StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView = {
        let view = StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView(
            appearance: .init(cornerRadius: self.appearance.cornerRadius)
        )
        return view
    }()

    private lazy var textLabel: UILabel = {
        Self.makeTextLabel(appearance: self.appearance)
    }()

    var text: String? {
        didSet {
            self.textLabel.text = self.text
        }
    }

    var isEnabled = true {
        didSet {
            self.isUserInteractionEnabled = self.isEnabled
        }
    }

    var state: StepQuizFillBlanksSelectOptionsCollectionViewCellContainerView.State {
        get {
            self.inputContainerView.state
        }
        set {
            self.inputContainerView.state = newValue
        }
    }

    override var isHighlighted: Bool {
        didSet {
            self.textLabel.alpha = self.isHighlighted ? 0.5 : 1.0
        }
    }

    override init(frame: CGRect) {
        super.init(frame: frame)

        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    static func calculatePreferredContentSize(text: String, maxWidth: CGFloat) -> CGSize {
        if Self.prototypeTextLabel == nil {
            Self.prototypeTextLabel = Self.makeTextLabel()
        }

        guard let label = Self.prototypeTextLabel else {
            return .zero
        }

        label.frame = CGRect(x: 0, y: 0, width: maxWidth, height: CGFloat.greatestFiniteMagnitude)

        label.text = text
        label.sizeToFit()

        let labelSize = label.bounds.size
        let appearance = Appearance()

        let widthWithInsets = appearance.insets.leading
          + labelSize.width.rounded(.up)
          + appearance.insets.trailing
        let heightWithInsets = appearance.insets.top
          + labelSize.height.rounded(.up)
          + appearance.insets.bottom

        let width = max(Appearance.defaultWidth, widthWithInsets)
        let height = max(Appearance.defaultHeight, heightWithInsets)

        return CGSize(width: width, height: height)
    }

    private static func makeTextLabel(appearance: Appearance = Appearance()) -> UILabel {
        let label = UILabel()
        label.font = Appearance.font
        label.textColor = appearance.textColor
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        return label
    }
}

extension StepQuizFillBlanksSelectOptionsCollectionViewCell: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        self.contentView.addSubview(self.inputContainerView)
        self.inputContainerView.addSubview(self.textLabel)
    }

    func makeConstraints() {
        self.inputContainerView.translatesAutoresizingMaskIntoConstraints = false
        self.inputContainerView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        self.textLabel.translatesAutoresizingMaskIntoConstraints = false
        self.textLabel.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.edges.equalToSuperview().inset(self.appearance.insets.uiEdgeInsets)
        }
    }
}
