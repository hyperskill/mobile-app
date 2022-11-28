import SnapKit
import SwiftUI
import UIKit

struct StepTextView: UIViewRepresentable {
    typealias UIViewType = StepTextUIKitView

    var text: String

    var appearance = StepTextUIKitView.Appearance()

    func makeUIView(context: Context) -> StepTextUIKitView {
        StepTextUIKitView(appearance: appearance)
    }

    func updateUIView(_ uiView: StepTextUIKitView, context: Context) {
        uiView.setText(text)
    }
}

struct StepTextView_Previews: PreviewProvider {
    static var previews: some View {
        StepTextView(
            text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
"""
        )
        .padding()
    }
}

// MARK: - StepTextUIKitView -

extension StepTextUIKitView {
    struct Appearance {
        var textFont = UIFont.preferredFont(forTextStyle: .body)
        var textColor = UIColor.primaryText

        let backgroundColor = UIColor.clear
    }
}

final class StepTextUIKitView: UIView {
    let appearance: Appearance

    private lazy var processedContentView: ProcessedContentView = {
        let processedContentViewAppearance = ProcessedContentView.Appearance(
            labelFont: appearance.textFont,
            backgroundColor: .clear
        )

        let contentProcessor = ContentProcessor(
            injections: ContentProcessor.defaultInjections + [
                StepStylesInjection(),
                FontInjection(font: appearance.textFont),
                TextColorInjection(dynamicColor: appearance.textColor)
            ]
        )

        let processedContentView = ProcessedContentView.init(
            frame: .zero,
            appearance: processedContentViewAppearance,
            contentProcessor: contentProcessor,
            htmlToAttributedStringConverter: HTMLToAttributedStringConverter(
                font: appearance.textFont
            )
        )
        processedContentView.delegate = self

        return processedContentView
    }()

    override var intrinsicContentSize: CGSize {
        CGSize(
            width: UIView.noIntrinsicMetric,
            height: processedContentView.intrinsicContentSize.height
        )
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

    func setText(_ text: String) {
        processedContentView.setText(text)
    }
}

// MARK: - StepTextUIKitView: ProgrammaticallyInitializableViewProtocol -

extension StepTextUIKitView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(processedContentView)
    }

    func makeConstraints() {
        processedContentView.translatesAutoresizingMaskIntoConstraints = false
        processedContentView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

// MARK: - StepTextUIKitView: ProcessedContentViewDelegate -

extension StepTextUIKitView: ProcessedContentViewDelegate {
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
