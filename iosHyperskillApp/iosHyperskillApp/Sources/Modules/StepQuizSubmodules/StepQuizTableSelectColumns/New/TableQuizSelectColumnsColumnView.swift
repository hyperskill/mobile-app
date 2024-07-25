import BEMCheckBox
import SnapKit
import UIKit

extension TableQuizSelectColumnsColumnView {
    struct Appearance {
        let checkBoxLineWidth: CGFloat = 2
        let checkBoxAnimationDuration: CGFloat = 0.5
        let checkBoxTintColor = ColorPalette.primary
        let checkBoxOnCheckColor = UIColor.white
        let checkBoxOnFillColor = ColorPalette.primary
        let checkBoxOnTintColor = ColorPalette.primary
        let checkBoxWidthHeight: CGFloat = 20
        let checkBoxInsets = LayoutInsets(leading: 16)

        let titleFont = UIFont.preferredFont(forTextStyle: .body)
        let titleTextColor = UIColor.primaryText
        let titleInsets = LayoutInsets.default

        let contentViewMinHeight: CGFloat = 44

        let backgroundColor = UIColor.clear
    }
}

final class TableQuizSelectColumnsColumnView: UIControl {
    let appearance: Appearance

    private lazy var checkBox: BEMCheckBox = {
        let checkBox = BEMCheckBox()
        checkBox.lineWidth = appearance.checkBoxLineWidth
        checkBox.hideBox = false
        checkBox.boxType = .circle
        checkBox.tintColor = appearance.checkBoxTintColor
        checkBox.onCheckColor = appearance.checkBoxOnCheckColor
        checkBox.onFillColor = appearance.checkBoxOnFillColor
        checkBox.onTintColor = appearance.checkBoxOnTintColor
        checkBox.animationDuration = appearance.checkBoxAnimationDuration
        checkBox.onAnimationType = .fill
        checkBox.offAnimationType = .fill
        return checkBox
    }()

    private lazy var titleProcessedContentView: ProcessedContentView = {
        let appearance = ProcessedContentView.Appearance(
            labelFont: appearance.titleFont,
            labelTextColor: appearance.titleTextColor,
            activityIndicatorViewColor: nil,
            insets: LayoutInsets(uiEdgeInsets: .zero),
            backgroundColor: .clear
        )

        let contentProcessor = ContentProcessor(
            rules: ContentProcessor.defaultRules,
            injections: ContentProcessor.defaultInjections + [
                FontInjection(font: self.appearance.titleFont),
                TextColorInjection(dynamicColor: self.appearance.titleTextColor)
            ]
        )

        let processedContentView = ProcessedContentView(
            frame: .zero,
            appearance: appearance,
            contentProcessor: contentProcessor,
            htmlToAttributedStringConverter: HTMLToAttributedStringConverter(font: self.appearance.titleFont)
        )
        processedContentView.delegate = self

        return processedContentView
    }()

    private lazy var contentView = UIView()

    private lazy var tapProxyView = UIKitTapProxyView(targetView: self)

    var isOn: Bool { self.checkBox.on }

    var onValueChanged: ((Bool) -> Void)?

    override var isHighlighted: Bool {
        didSet {
            self.titleProcessedContentView.alpha = self.isHighlighted ? 0.5 : 1.0
        }
    }

    override var intrinsicContentSize: CGSize {
        let titleProcessedContentViewIntrinsicContentSize = self.titleProcessedContentView.intrinsicContentSize
        let titleProcessedContentViewHeightWithInsets = titleProcessedContentViewIntrinsicContentSize.height
            + self.appearance.titleInsets.top
            + self.appearance.titleInsets.bottom

        let height = max(self.appearance.contentViewMinHeight, titleProcessedContentViewHeightWithInsets)

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

    override func layoutSubviews() {
        super.layoutSubviews()
        self.invalidateIntrinsicContentSize()
    }

    func setOn(_ isOn: Bool, animated: Bool) {
        self.checkBox.setOn(isOn, animated: animated)
    }

    func setTitle(_ title: String) {
        self.titleProcessedContentView.setText(title)
    }

    @objc
    private func clicked() {
        let newValue = !self.checkBox.on
        self.onValueChanged?(newValue)
    }
}

extension TableQuizSelectColumnsColumnView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
        self.contentView.backgroundColor = self.appearance.backgroundColor

        self.addTarget(self, action: #selector(self.clicked), for: .touchUpInside)
    }

    func addSubviews() {
        self.addSubview(self.contentView)
        self.contentView.addSubview(self.checkBox)
        self.contentView.addSubview(self.titleProcessedContentView)

        self.addSubview(self.tapProxyView)
    }

    func makeConstraints() {
        self.contentView.translatesAutoresizingMaskIntoConstraints = false
        self.contentView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.height.greaterThanOrEqualTo(self.appearance.contentViewMinHeight)
        }

        self.checkBox.translatesAutoresizingMaskIntoConstraints = false
        self.checkBox.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(self.appearance.checkBoxInsets.leading)
            make.centerY.equalToSuperview()
            make.width.height.equalTo(self.appearance.checkBoxWidthHeight)
        }

        self.titleProcessedContentView.translatesAutoresizingMaskIntoConstraints = false
        self.titleProcessedContentView.snp.makeConstraints { make in
            make.top.greaterThanOrEqualToSuperview().offset(self.appearance.titleInsets.top)
            make.leading.equalTo(self.checkBox.snp.trailing).offset(self.appearance.titleInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-self.appearance.titleInsets.bottom)
            make.trailing.equalToSuperview().offset(-self.appearance.titleInsets.trailing)
            make.centerY.equalToSuperview()
        }

        self.tapProxyView.translatesAutoresizingMaskIntoConstraints = false
        self.tapProxyView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

extension TableQuizSelectColumnsColumnView: ProcessedContentViewDelegate {
    func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
        invalidateIntrinsicContentSize()
    }

    func processedContentView(_ view: ProcessedContentView, didReportNewHeight height: Int) {
        invalidateIntrinsicContentSize()
    }

    func processedContentView(_ view: ProcessedContentView, didOpenImageURL url: URL) {}

    func processedContentView(_ view: ProcessedContentView, didOpenLink url: URL) {}
}

#if DEBUG
@available(iOS 17.0, *)
#Preview {
    let view = TableQuizSelectColumnsColumnView()
    view.setTitle("test")

    DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
        view.setOn(true, animated: true)
    }

    return view
}
#endif
