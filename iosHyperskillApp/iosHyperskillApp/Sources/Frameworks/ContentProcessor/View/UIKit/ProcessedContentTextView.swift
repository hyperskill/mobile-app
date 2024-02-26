import Atributika
import SnapKit
import UIKit

protocol ProcessedContentTextViewDelegate: AnyObject {
    func processedContentTextView(_ view: ProcessedContentTextView, didReportNewHeight height: CGFloat)
}

extension ProcessedContentTextView {
    struct Appearance {
        var font = UIFont.systemFont(ofSize: 17, weight: .regular)
        var textColor = UIColor.primaryText
    }
}

final class ProcessedContentTextView: UIView {
    let appearance: Appearance

    weak var delegate: ProcessedContentTextViewDelegate?

    private let htmlToAttributedStringConverter: HTMLToAttributedStringConverterProtocol

    private lazy var textView: UITextView = {
        let textView = UITextView()

        textView.font = appearance.font
        textView.textColor = appearance.textColor

        textView.textContainer.lineBreakMode = .byWordWrapping
        textView.textContainer.maximumNumberOfLines = 0
        textView.textContainer.lineFragmentPadding = 0

        textView.isSelectable = true
        textView.isUserInteractionEnabled = true

        textView.textContainerInset = .zero
        textView.isEditable = false
        textView.isScrollEnabled = false
        textView.backgroundColor = nil

        textView.dataDetectorTypes = [.link]

        return textView
    }()

    private lazy var attributedLabel: AttributedLabel = {
        let attributedLabel = AttributedLabel()
        attributedLabel.font = appearance.font
        attributedLabel.textColor = appearance.textColor
        attributedLabel.numberOfLines = 0
        attributedLabel.isSelectable = true
        attributedLabel.onClick = { [weak self] (label, detection) in
            self?.handleAttributedLabelClicked(label: label, detection: detection)
        }
        return attributedLabel
    }()

    private var didSetupTextView = false
    private var didSetupAttributedLabel = false

    private var currentIntrinsicContentHeight: CGFloat = UIView.noIntrinsicMetric {
        didSet {
            if oldValue != self.currentIntrinsicContentHeight {
                self.delegate?.processedContentTextView(self, didReportNewHeight: self.currentIntrinsicContentHeight)
            }
        }
    }

    var onLinkClick: ((URL) -> Void)?

    var text: String? {
        didSet {
            if oldValue == self.text {
                return
            }

            guard let text = self.text else {
                return self.clearText()
            }

            if self.isAttributedLabelSupportNeeded(text: text) {
                self.setAttributedLabelText(text)
            } else {
                self.setLabelText(text)
            }

            self.invalidateIntrinsicContentSize()
        }
    }

    override var intrinsicContentSize: CGSize {
        let height: CGFloat = if self.didSetupTextView && !self.textView.isHidden {
            self.textView.sizeThatFits(CGSize(width: self.bounds.width, height: .infinity)).height
        } else if self.didSetupAttributedLabel {
            self.attributedLabel.sizeThatFits(CGSize(width: self.bounds.width, height: .infinity)).height
        } else {
            UIView.noIntrinsicMetric
        }

        self.currentIntrinsicContentHeight = height

        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance(),
        htmlToAttributedStringConverter: HTMLToAttributedStringConverterProtocol
    ) {
        self.appearance = appearance
        self.htmlToAttributedStringConverter = htmlToAttributedStringConverter
        super.init(frame: frame)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func sizeThatFits(_ size: CGSize) -> CGSize {
        let resSize: CGSize = if self.didSetupTextView && !self.textView.isHidden {
            self.textView.sizeThatFits(size)
        } else if self.didSetupAttributedLabel {
            self.attributedLabel.sizeThatFits(size)
        } else {
            CGSize(width: UIView.noIntrinsicMetric, height: UIView.noIntrinsicMetric)
        }

        return CGSize(
            width: ceil(resSize.width),
            height: ceil(resSize.height)
        )
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        self.invalidateIntrinsicContentSize()
    }

    private func clearText() {
        if self.didSetupTextView && !self.textView.isHidden {
            self.textView.text = nil
        } else {
            self.attributedLabel.attributedText = nil
        }
    }

    private func isAttributedLabelSupportNeeded(text: String) -> Bool {
        let hasAnyURLScheme = text.contains("http://") || text.contains("https://")
        let hasAnyHTMLTagStartEndSymbol = text.contains("<") || text.contains(">") || text.contains("</")
        return hasAnyURLScheme || hasAnyHTMLTagStartEndSymbol
    }

    private func setLabelText(_ text: String) {
        if !self.didSetupTextView {
            self.didSetupTextView = true
            self.setupTextView()
        }

        if self.didSetupAttributedLabel {
            self.attributedLabel.isHidden = true
            self.attributedLabel.attributedText = nil
        }

        self.textView.isHidden = false
        self.textView.text = text
    }

    private func setAttributedLabelText(_ text: String) {
        if !self.didSetupAttributedLabel {
            self.didSetupAttributedLabel = true
            self.setupAttributedLabel()
        }

        if self.didSetupTextView {
            self.textView.isHidden = true
            self.textView.text = nil
        }

        self.attributedLabel.isHidden = false
        self.attributedLabel.attributedText = self.htmlToAttributedStringConverter.convertToAttributedText(
            htmlString: text
        )
    }

    private func handleAttributedLabelClicked(label: AttributedLabel, detection: Detection) {
        switch detection.type {
        case .link(let url):
            self.onLinkClick?(url)
        case .tag(let tag):
            if tag.name == "a",
               let href = tag.attributes["href"],
               let url = URL(string: href) {
                self.onLinkClick?(url)
            }
        default:
            break
        }
    }

    private func setupTextView() {
        self.addSubview(self.textView)
        self.textView.translatesAutoresizingMaskIntoConstraints = false
        self.textView.snp.makeConstraints { $0.edges.equalToSuperview() }
    }

    private func setupAttributedLabel() {
        self.addSubview(self.attributedLabel)
        self.attributedLabel.translatesAutoresizingMaskIntoConstraints = false
        self.attributedLabel.snp.makeConstraints { $0.edges.equalToSuperview() }
    }
}
