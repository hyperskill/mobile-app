import SnapKit
import UIKit

extension CodeCompletionCellView {
    struct Appearance {
        let textColor = UIColor.primaryText
        let textInsets = LayoutInsets(top: 4, leading: 10, bottom: 4, trailing: 0)
    }
}

final class CodeCompletionCellView: UIView {
    let appearance: Appearance

    private lazy var textLabel: UILabel = {
        let label = UILabel()
        label.textColor = self.appearance.textColor
        label.numberOfLines = 1
        return label
    }()

    var attributedText: NSAttributedString? {
        didSet {
            textLabel.attributedText = attributedText
            invalidateIntrinsicContentSize()
        }
    }

    override var intrinsicContentSize: CGSize {
        let height = appearance.textInsets.top
            + textLabel.intrinsicContentSize.height
            + appearance.textInsets.bottom
        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

extension CodeCompletionCellView: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        addSubview(textLabel)
    }

    func makeConstraints() {
        textLabel.translatesAutoresizingMaskIntoConstraints = false
        textLabel.snp.makeConstraints { make in
            make.edges.equalToSuperview().inset(appearance.textInsets.uiEdgeInsets)
        }
    }
}
