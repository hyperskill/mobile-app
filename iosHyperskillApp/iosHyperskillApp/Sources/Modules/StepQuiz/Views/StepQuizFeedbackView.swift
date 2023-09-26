import SnapKit
import SwiftUI
import UIKit

struct StepQuizFeedbackView: UIViewRepresentable {
    typealias UIViewType = StepQuizFeedbackUIKitView

    var text: String

    func makeUIView(context: Context) -> StepQuizFeedbackUIKitView {
        StepQuizFeedbackUIKitView()
    }

    func updateUIView(_ uiView: StepQuizFeedbackUIKitView, context: Context) {
        uiView.setText(text)
    }
}

struct StepQuizFeedbackView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizFeedbackView(
            text: """
That's right! Since any comparison results in a boolean value, there is no need to write everything twice.
"""
        )
        .padding()
    }
}

// MARK: - StepQuizFeedbackUIKitView -

extension StepQuizFeedbackUIKitView {
    struct Appearance {
        let titleLabelTextFont = UIFont.preferredFont(forTextStyle: .caption1)
        let titleLabelTextColor = UIColor.tertiaryText

        let processedContentTextFont = UIFont.monospacedSystemFont(ofSize: 14, weight: .regular)
        let processedContentTextColor = UIColor.primaryText

        let padding = LayoutInsets.defaultInset
        let spacing = LayoutInsets.smallInset

        let borderColor = ColorPalette.onSurfaceAlpha12
        let borderWidth: CGFloat = 1
        let borderCornerRadius: CGFloat = 8

        let backgroundColor = ColorPalette.background
    }
}

final class StepQuizFeedbackUIKitView: UIView {
    let appearance: Appearance

    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = appearance.titleLabelTextFont
        label.textColor = appearance.titleLabelTextColor
        label.numberOfLines = 1
        label.text = Strings.StepQuiz.feedbackTitle
        return label
    }()

    private lazy var processedContentView: ProcessedContentView = {
        let processedContentViewAppearance = ProcessedContentView.Appearance(
            labelFont: appearance.processedContentTextFont,
            backgroundColor: .clear
        )

        let contentProcessor = ContentProcessor(
            injections: ContentProcessor.defaultInjections + [
                FontInjection(font: appearance.processedContentTextFont),
                TextColorInjection(dynamicColor: appearance.processedContentTextColor)
            ]
        )

        let processedContentView = ProcessedContentView(
            frame: .zero,
            appearance: processedContentViewAppearance,
            contentProcessor: contentProcessor,
            htmlToAttributedStringConverter: HTMLToAttributedStringConverter(
                font: appearance.processedContentTextFont
            )
        )
        processedContentView.delegate = self

        return processedContentView
    }()

    override var intrinsicContentSize: CGSize {
        let titleLabelHeight = titleLabel.intrinsicContentSize.height
        let processedContentViewHeight = processedContentView.intrinsicContentSize.height

        let height =
          appearance.padding + titleLabelHeight + appearance.spacing + processedContentViewHeight + appearance.padding

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

    override func layoutSubviews() {
        clipsToBounds = true
        layer.cornerRadius = appearance.borderCornerRadius
        layer.borderColor = appearance.borderColor.cgColor
        layer.borderWidth = appearance.borderWidth
    }

    func setText(_ text: String) {
        processedContentView.setText(text)
    }
}

// MARK: - StepQuizFeedbackUIKitView: ProgrammaticallyInitializableViewProtocol -

extension StepQuizFeedbackUIKitView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(titleLabel)
        addSubview(processedContentView)
    }

    func makeConstraints() {
        titleLabel.translatesAutoresizingMaskIntoConstraints = false
        titleLabel.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(appearance.padding)
            make.leading.equalToSuperview().offset(appearance.padding)
            make.trailing.lessThanOrEqualToSuperview().offset(-appearance.padding)
        }

        processedContentView.translatesAutoresizingMaskIntoConstraints = false
        processedContentView.snp.makeConstraints { make in
            make.top.equalTo(titleLabel.snp.bottom).offset(appearance.spacing)
            make.leading.equalToSuperview().offset(appearance.padding)
            make.bottom.equalToSuperview().offset(-appearance.padding)
            make.trailing.equalToSuperview().offset(-appearance.padding)
        }
    }
}

// MARK: - StepQuizFeedbackUIKitView: ProcessedContentViewDelegate -

extension StepQuizFeedbackUIKitView: ProcessedContentViewDelegate {
    func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
        invalidateLayout()
    }

    func processedContentView(_ view: ProcessedContentView, didReportNewHeight height: Int) {
        invalidateLayout()
    }

    func processedContentView(_ view: ProcessedContentView, didOpenImageURL url: URL) {
        openURLInTheWeb(url)
    }

    func processedContentView(_ view: ProcessedContentView, didOpenLink url: URL) {
        openURLInTheWeb(url)
    }

    // MARK: Private Helpers

    private func invalidateLayout() {
        DispatchQueue.main.async {
            self.layoutIfNeeded()
            self.invalidateIntrinsicContentSize()
        }
    }

    private func openURLInTheWeb(_ url: URL) {
        WebControllerManager.shared.presentWebControllerWithURL(
            url,
            withKey: .externalLink,
            controllerType: .inAppSafari
        )
    }
}
