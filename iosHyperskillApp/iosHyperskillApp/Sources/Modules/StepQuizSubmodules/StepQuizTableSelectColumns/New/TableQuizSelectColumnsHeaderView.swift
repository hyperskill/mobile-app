import SnapKit
import UIKit

extension TableQuizSelectColumnsHeaderView {
    struct Appearance {
        let promptFont = UIFont.preferredFont(forTextStyle: .caption1)
        let promptTextColor = UIColor.secondaryText

        let titleFont = UIFont.preferredFont(forTextStyle: .body)
        let titleTextColor = UIColor.primaryText

        let contentStackViewSpacing: CGFloat = LayoutInsets.defaultInset
        let contentStackViewInsets = LayoutInsets.default.uiEdgeInsets

        let backgroundColor = UIColor.clear
    }
}

final class TableQuizSelectColumnsHeaderView: UIView {
    let appearance: Appearance

    private lazy var promptLabel: UILabel = {
        let label = UILabel()
        label.font = appearance.promptFont
        label.textColor = appearance.promptTextColor
        label.numberOfLines = 1
        label.textAlignment = .center
        return label
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

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        return stackView
    }()

    private lazy var separatorView = UIKitSeparatorView()

    var prompt: String? {
        didSet {
            self.promptLabel.text = self.prompt
            self.promptLabel.isHidden = self.prompt?.isEmpty ?? true
        }
    }

    var title: String? {
        didSet {
            self.titleProcessedContentView.setText(self.title)
        }
    }

    override var intrinsicContentSize: CGSize {
        let contentStackViewIntrinsicContentSize = self.contentStackView
            .systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let contentStackViewHeightWithInsets = contentStackViewIntrinsicContentSize.height
            + self.appearance.contentStackViewInsets.top
            + self.appearance.contentStackViewInsets.bottom

        let height = contentStackViewHeightWithInsets.rounded(.up)

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
}

extension TableQuizSelectColumnsHeaderView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.contentStackView)
        self.contentStackView.addArrangedSubview(self.promptLabel)
        self.contentStackView.addArrangedSubview(self.titleProcessedContentView)

        self.addSubview(self.separatorView)
    }

    func makeConstraints() {
        self.contentStackView.translatesAutoresizingMaskIntoConstraints = false
        self.contentStackView.snp.makeConstraints { make in
            make.edges.equalToSuperview().inset(self.appearance.contentStackViewInsets)
        }

        self.separatorView.translatesAutoresizingMaskIntoConstraints = false
        self.separatorView.snp.makeConstraints { make in
            make.leading.bottom.trailing.equalToSuperview()
        }
    }
}

extension TableQuizSelectColumnsHeaderView: ProcessedContentViewDelegate {
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
    let view = TableQuizSelectColumnsHeaderView()
    view.prompt = Strings.StepQuizTable.multipleChoicePrompt
    view.title = "Title goes here"
    return view
}
#endif
