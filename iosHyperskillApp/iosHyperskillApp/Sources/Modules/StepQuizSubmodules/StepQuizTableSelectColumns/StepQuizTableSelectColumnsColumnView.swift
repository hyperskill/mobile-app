import BEMCheckBox
import SnapKit
import UIKit

extension StepQuizTableSelectColumnsColumnView {
    struct Appearance {
        var checkBoxBoxType: BEMBoxType = .circle
        let checkBoxLineWidth: CGFloat = 2
        let checkBoxAnimationDuration: CGFloat =
            StepQuizTableSelectColumnsViewController.Animation.dismissAnimationDelayAfterChoiceSelected
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

final class StepQuizTableSelectColumnsColumnView: UIControl {
    let appearance: Appearance

    private lazy var checkBox: BEMCheckBox = {
        let checkBox = BEMCheckBox()
        checkBox.lineWidth = appearance.checkBoxLineWidth
        checkBox.hideBox = false
        checkBox.boxType = appearance.checkBoxBoxType
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
        let processedContentViewAppearance = ProcessedContentView.Appearance(
            labelFont: appearance.titleFont,
            labelTextColor: appearance.titleTextColor,
            backgroundColor: .clear
        )

        let contentProcessor = ContentProcessor(
            injections: ContentProcessor.defaultInjections + [
                FontInjection(font: appearance.titleFont),
                TextColorInjection(dynamicColor: appearance.titleTextColor)
            ]
        )

        let processedContentView = ProcessedContentView(
            frame: .zero,
            appearance: processedContentViewAppearance,
            contentProcessor: contentProcessor,
            htmlToAttributedStringConverter: HTMLToAttributedStringConverter(font: appearance.titleFont)
        )
        processedContentView.delegate = self

        return processedContentView
    }()

    private lazy var contentView = UIView()

    private lazy var tapProxyView = UIKitTapProxyView(targetView: self)

    var isOn: Bool { checkBox.on }

    var onValueChanged: ((Bool) -> Void)?

    var onContentLoad: (() -> Void)?

    override var isHighlighted: Bool {
        didSet {
            titleProcessedContentView.alpha = isHighlighted ? 0.5 : 1.0
        }
    }

    override var intrinsicContentSize: CGSize {
        let titleProcessedContentViewIntrinsicContentSize = titleProcessedContentView.intrinsicContentSize
        let titleProcessedContentViewHeightWithInsets = titleProcessedContentViewIntrinsicContentSize.height
            + appearance.titleInsets.top
            + appearance.titleInsets.bottom

        let height = max(appearance.contentViewMinHeight, titleProcessedContentViewHeightWithInsets)

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
        invalidateIntrinsicContentSize()
    }

    func setOn(_ isOn: Bool, animated: Bool) {
        checkBox.setOn(isOn, animated: animated)
    }

    func setTitle(_ title: String) {
        titleProcessedContentView.setText(title)
    }

    @objc
    private func clicked() {
        let newValue = !checkBox.on
        onValueChanged?(newValue)
    }
}

extension StepQuizTableSelectColumnsColumnView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
        contentView.backgroundColor = appearance.backgroundColor

        addTarget(self, action: #selector(clicked), for: .touchUpInside)
    }

    func addSubviews() {
        addSubview(contentView)
        contentView.addSubview(checkBox)
        contentView.addSubview(titleProcessedContentView)

        addSubview(tapProxyView)
    }

    func makeConstraints() {
        contentView.translatesAutoresizingMaskIntoConstraints = false
        contentView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
            make.height.greaterThanOrEqualTo(appearance.contentViewMinHeight)
        }

        checkBox.translatesAutoresizingMaskIntoConstraints = false
        checkBox.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(appearance.checkBoxInsets.leading)
            make.centerY.equalToSuperview()
            make.width.height.equalTo(appearance.checkBoxWidthHeight)
        }

        titleProcessedContentView.translatesAutoresizingMaskIntoConstraints = false
        titleProcessedContentView.snp.makeConstraints { make in
            make.top.greaterThanOrEqualToSuperview().offset(appearance.titleInsets.top)
            make.leading.equalTo(checkBox.snp.trailing).offset(appearance.titleInsets.leading)
            make.bottom.lessThanOrEqualToSuperview().offset(-appearance.titleInsets.bottom)
            make.trailing.equalToSuperview().offset(-appearance.titleInsets.trailing)
            make.centerY.equalToSuperview()
        }

        tapProxyView.translatesAutoresizingMaskIntoConstraints = false
        tapProxyView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

extension StepQuizTableSelectColumnsColumnView: ProcessedContentViewDelegate {
    func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
        invalidateIntrinsicContentSize()
        onContentLoad?()
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
    let view = StepQuizTableSelectColumnsColumnView()
    view.setTitle("test")

    DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
        view.setOn(true, animated: true)
    }

    return view
}
#endif
