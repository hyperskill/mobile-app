import SnapKit
import SwiftUI
import UIKit

// MARK: - StepTextView -

struct StepTextView: UIViewRepresentable {
    typealias UIViewType = StepTextUIKitView

    var text: String

    var appearance = StepTextUIKitView.Appearance()

    var onContentLoaded: (() -> Void)?

    // MARK: UIViewRepresentable

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> StepTextUIKitView {
        let view = StepTextUIKitView(appearance: appearance)
        view.processedContentView.delegate = context.coordinator
        return view
    }

    func updateUIView(_ uiView: StepTextUIKitView, context: Context) {
        uiView.setText(text)

        context.coordinator.onContentLoaded = { [weak uiView] in
            uiView?.invalidateLayout()
            onContentLoaded?()
        }
        context.coordinator.onHeightUpdated = { [weak uiView] _ in
            uiView?.invalidateLayout()
        }
        context.coordinator.onOpenImageURL = Self.openURLInTheWeb
        context.coordinator.onOpenLink = Self.openURLInTheWeb
    }

    // MARK: Private Helpers

    private static func openURLInTheWeb(_ url: URL) {
        WebControllerManager.shared.presentWebControllerWithURL(
            url,
            withKey: .externalLink,
            controllerType: .inAppSafari
        )
    }
}

// MARK: StepTextView (Coordinator)

extension StepTextView {
    class Coordinator: NSObject, ProcessedContentViewDelegate {
        var onContentLoaded: (() -> Void)?

        var onHeightUpdated: ((Int) -> Void)?

        var onOpenImageURL: ((URL) -> Void)?

        var onOpenLink: ((URL) -> Void)?

        func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
            onContentLoaded?()
        }

        func processedContentView(_ view: ProcessedContentView, didReportNewHeight height: Int) {
            onHeightUpdated?(height)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenImageURL url: URL) {
            onOpenImageURL?(url)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenLink url: URL) {
            onOpenLink?(url)
        }
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

    fileprivate lazy var processedContentView: ProcessedContentView = {
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

    fileprivate func invalidateLayout() {
        DispatchQueue.main.async {
            self.layoutIfNeeded()
            self.invalidateIntrinsicContentSize()
        }
    }
}

// MARK: StepTextUIKitView: ProgrammaticallyInitializableViewProtocol

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
