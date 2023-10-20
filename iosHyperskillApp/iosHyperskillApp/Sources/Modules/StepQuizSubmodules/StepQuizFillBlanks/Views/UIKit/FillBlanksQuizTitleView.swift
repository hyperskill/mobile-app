import SnapKit
import UIKit

extension FillBlanksQuizTitleView {
    struct Appearance {
        let textColor = UIColor.primaryText
        let font = UIFont.preferredFont(forTextStyle: .headline)
        let insets = LayoutInsets(horizontal: LayoutInsets.defaultInset, vertical: LayoutInsets.smallInset)

        var backgroundColor = ColorPalette.background
    }
}

final class FillBlanksQuizTitleView: UIView {
    let appearance: Appearance

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = Strings.StepQuizFillBlanks.title
        label.textColor = self.appearance.textColor
        label.font = self.appearance.font
        label.numberOfLines = 1
        return label
    }()

    private lazy var topSeparatorView = UIKitSeparatorView()
    private lazy var bottomSeparatorView = UIKitSeparatorView()

    override var intrinsicContentSize: CGSize {
        let titleLabelHeight = self.titleLabel.intrinsicContentSize.height
        let height = self.appearance.insets.top + titleLabelHeight + self.appearance.insets.bottom
        return CGSize(width: UIView.noIntrinsicMetric, height: height)
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
}

extension FillBlanksQuizTitleView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.titleLabel)
        self.addSubview(self.topSeparatorView)
        self.addSubview(self.bottomSeparatorView)
    }

    func makeConstraints() {
        self.titleLabel.translatesAutoresizingMaskIntoConstraints = false
        self.titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(self.appearance.insets.top)
            make.leading.equalToSuperview().offset(self.appearance.insets.leading)
            make.bottom.equalToSuperview().offset(-self.appearance.insets.bottom)
            make.trailing.equalToSuperview().offset(-self.appearance.insets.trailing)
        }

        self.topSeparatorView.translatesAutoresizingMaskIntoConstraints = false
        self.topSeparatorView.snp.makeConstraints { make in
            make.top.leading.trailing.equalToSuperview()
        }

        self.bottomSeparatorView.translatesAutoresizingMaskIntoConstraints = false
        self.bottomSeparatorView.snp.makeConstraints { make in
            make.leading.bottom.trailing.equalToSuperview()
        }
    }
}

@available(iOS 17, *)
#Preview {
    FillBlanksQuizTitleView()
}
