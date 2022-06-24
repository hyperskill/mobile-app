import SnapKit
import UIKit

extension CodeInputAccessoryCollectionViewCell {
    enum Appearance {
        static let textColor = UIColor.primaryText

        static let cornerRadius: CGFloat = 4
        static let borderWidth: CGFloat = 1
        static let borderColor = UIColor.clear.cgColor

        static let backgroundColor = UIColor.tertiarySystemBackground

        static let calculateWidthBoundsWidthPadding: CGFloat = 10
    }
}

final class CodeInputAccessoryCollectionViewCell: UICollectionViewCell, Reusable {
    private lazy var textLabel = Self.makeTextLabel()

    override init(frame: CGRect) {
        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    func configure(text: String, size: CodeInputAccessorySize) {
        textLabel.text = text
        textLabel.font = Self.makeFont(for: size)
    }

    static func calculateWidth(for text: String, size: CodeInputAccessorySize) -> CGFloat {
        let label = makeTextLabel(
            frame: CGRect(x: 0, y: 0, width: CGFloat.greatestFiniteMagnitude, height: CGFloat.greatestFiniteMagnitude),
            size: size
        )
        label.text = text
        label.sizeToFit()
        return max(size.realSizes.minAccessoryWidth, label.bounds.width + Appearance.calculateWidthBoundsWidthPadding)
    }

    private static func makeTextLabel(frame: CGRect = .zero, size: CodeInputAccessorySize? = nil) -> UILabel {
        let label = UILabel(frame: frame)
        label.textColor = Appearance.textColor
        label.numberOfLines = 1
        label.textAlignment = .center

        if let size = size {
            label.font = makeFont(for: size)
        }

        return label
    }

    private static func makeFont(for size: CodeInputAccessorySize) -> UIFont {
        UIFont(name: "Courier", size: size.realSizes.textSize) ?? .systemFont(ofSize: size.realSizes.textSize)
    }
}

extension CodeInputAccessoryCollectionViewCell: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        layer.cornerRadius = Appearance.cornerRadius
        layer.borderWidth = Appearance.borderWidth
        layer.borderColor = Appearance.borderColor
        layer.masksToBounds = true

        backgroundColor = Appearance.backgroundColor
    }

    func addSubviews() {
        contentView.addSubview(textLabel)
    }

    func makeConstraints() {
        textLabel.translatesAutoresizingMaskIntoConstraints = false
        textLabel.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}
