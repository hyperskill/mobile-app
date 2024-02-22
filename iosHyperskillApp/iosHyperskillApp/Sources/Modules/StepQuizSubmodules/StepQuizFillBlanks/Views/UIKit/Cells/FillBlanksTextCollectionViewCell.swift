import SnapKit
import UIKit

extension FillBlanksTextCollectionViewCell {
    struct Appearance {
        let font = UIFont.preferredFont(forTextStyle: .body)
        let textColor = UIColor.primaryText
    }
}

final class FillBlanksTextCollectionViewCell: UICollectionViewCell, Reusable {
    private static var prototypeTextLabel: UILabel?

    private lazy var textLabel: UILabel = {
        Self.makeTextLabel(appearance: appearance)
    }()

    var appearance = Appearance()

    var attributedText: NSAttributedString? {
        didSet {
            if let attributedText = self.attributedText {
                self.textLabel.attributedText = attributedText
            } else {
                self.textLabel.attributedText = nil
            }
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

    deinit {
        Self.prototypeTextLabel = nil
    }

    static func calculatePreferredContentSize(attributedText: NSAttributedString?, maxWidth: CGFloat) -> CGSize {
        if Self.prototypeTextLabel == nil {
            Self.prototypeTextLabel = Self.makeTextLabel()
        }

        guard let label = Self.prototypeTextLabel else {
            return .zero
        }

        label.frame = CGRect(x: 0, y: 0, width: maxWidth, height: CGFloat.greatestFiniteMagnitude)

        label.attributedText = attributedText
        label.sizeToFit()

        var size = label.bounds.size
        size.width = size.width.rounded(.up)
        size.height = size.height.rounded(.up)

        return size
    }

    private static func makeTextLabel(appearance: Appearance = Appearance()) -> UILabel {
        let label = UILabel()
        label.font = appearance.font
        label.textColor = appearance.textColor
        label.numberOfLines = 0
        label.lineBreakMode = .byWordWrapping
        return label
    }
}

extension FillBlanksTextCollectionViewCell: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        self.contentView.addSubview(self.textLabel)
    }

    func makeConstraints() {
        self.textLabel.translatesAutoresizingMaskIntoConstraints = false
        self.textLabel.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}
