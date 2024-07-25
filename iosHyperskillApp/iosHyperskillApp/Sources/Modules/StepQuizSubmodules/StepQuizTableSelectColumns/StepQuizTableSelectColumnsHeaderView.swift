import SnapKit
import UIKit

extension StepQuizTableSelectColumnsHeaderView {
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

final class StepQuizTableSelectColumnsHeaderView: UIView {
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

    private lazy var contentStackView: UIStackView = {
        let stackView = UIStackView()
        stackView.axis = .vertical
        stackView.spacing = appearance.contentStackViewSpacing
        return stackView
    }()

    private lazy var separatorView = UIKitSeparatorView()

    var prompt: String? {
        didSet {
            promptLabel.text = prompt
            promptLabel.isHidden = prompt?.isEmpty ?? true
        }
    }

    var title: String? {
        didSet {
            titleProcessedContentView.setText(title)
        }
    }

    override var intrinsicContentSize: CGSize {
        let contentStackViewIntrinsicContentSize = contentStackView
            .systemLayoutSizeFitting(UIView.layoutFittingCompressedSize)
        let contentStackViewHeightWithInsets = contentStackViewIntrinsicContentSize.height
            + appearance.contentStackViewInsets.top
            + appearance.contentStackViewInsets.bottom

        let height = contentStackViewHeightWithInsets.rounded(.up)

        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    var onContentLoad: (() -> Void)?

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
}

extension StepQuizTableSelectColumnsHeaderView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(contentStackView)
        contentStackView.addArrangedSubview(promptLabel)
        contentStackView.addArrangedSubview(titleProcessedContentView)

        addSubview(separatorView)
    }

    func makeConstraints() {
        contentStackView.translatesAutoresizingMaskIntoConstraints = false
        contentStackView.snp.makeConstraints { make in
            make.edges.equalToSuperview().inset(appearance.contentStackViewInsets)
        }

        separatorView.translatesAutoresizingMaskIntoConstraints = false
        separatorView.snp.makeConstraints { make in
            make.leading.bottom.trailing.equalToSuperview()
        }
    }
}

extension StepQuizTableSelectColumnsHeaderView: ProcessedContentViewDelegate {
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
    let view = StepQuizTableSelectColumnsHeaderView()
    view.prompt = Strings.StepQuizTable.multipleChoicePrompt
    view.title = "Title goes here"
    return view
}
#endif
