import SwiftUI
import UIKit

struct LatexView: UIViewRepresentable {
    let text: String

    var configuration = Configuration()

    var onContentLoaded: (() -> Void)?
    var onHeightUpdated: ((Int) -> Void)?

    var onOpenImageURL: (URL) -> Void = Self.openURLInTheWeb(_:)
    var onOpenLink: (URL) -> Void = Self.openURLInTheWeb(_:)

    // MARK: UIViewRepresentable

    static func dismantleUIView(_ uiView: ProcessedContentView, coordinator: Coordinator) {
        uiView.delegate = nil

        coordinator.onContentLoaded = nil
        coordinator.onHeightUpdated = nil
        coordinator.onOpenImageURL = nil
        coordinator.onOpenLink = nil
    }

    func makeCoordinator() -> Coordinator {
        Coordinator()
    }

    func makeUIView(context: Context) -> ProcessedContentView {
        let processedContentView = ProcessedContentView(
            appearance: self.configuration.appearance,
            contentProcessor: self.configuration.contentProcessor,
            htmlToAttributedStringConverter: self.configuration.htmlToAttributedStringConverter
        )

        processedContentView.delegate = context.coordinator

        return processedContentView
    }

    func updateUIView(_ processedContentView: ProcessedContentView, context: Context) {
        let coordinator = context.coordinator

        if coordinator.currentTextHashValue != self.text.hashValue {
            processedContentView.setText(self.text)
        }
        coordinator.currentTextHashValue = self.text.hashValue

        coordinator.onContentLoaded = self.onContentLoaded
        coordinator.onHeightUpdated = self.onHeightUpdated
        coordinator.onOpenImageURL = self.onOpenImageURL
        coordinator.onOpenLink = self.onOpenLink
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

// MARK: - LatexView (Coordinator) -

extension LatexView {
    class Coordinator: NSObject, ProcessedContentViewDelegate {
        var onContentLoaded: (() -> Void)?
        var onHeightUpdated: ((Int) -> Void)?

        var onOpenImageURL: ((URL) -> Void)?
        var onOpenLink: ((URL) -> Void)?

        fileprivate var currentTextHashValue: Int?

        func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
            invalidateProcessedContentViewLayout(view)
            self.onContentLoaded?()
        }

        func processedContentView(_ view: ProcessedContentView, didReportNewHeight height: Int) {
            invalidateProcessedContentViewLayout(view)
            self.onHeightUpdated?(height)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenImageURL url: URL) {
            self.onOpenImageURL?(url)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenLink url: URL) {
            self.onOpenLink?(url)
        }

        private func invalidateProcessedContentViewLayout(_ view: ProcessedContentView) {
            DispatchQueue.main.async {
                view.layoutIfNeeded()
                view.invalidateIntrinsicContentSize()
            }
        }
    }
}

// MARK: - LatexView (Configuration) -

extension LatexView {
    struct Configuration {
        let appearance: ProcessedContentView.Appearance

        let contentProcessor: ContentProcessorProtocol

        let htmlToAttributedStringConverter: HTMLToAttributedStringConverterProtocol

        init(
            appearance: ProcessedContentView.Appearance = ProcessedContentView.Appearance(),
            contentProcessor: ContentProcessorProtocol = ContentProcessor(),
            htmlToAttributedStringConverter: HTMLToAttributedStringConverterProtocol
        ) {
            self.appearance = appearance
            self.contentProcessor = contentProcessor
            self.htmlToAttributedStringConverter = htmlToAttributedStringConverter
        }

        init(
            appearance: ProcessedContentView.Appearance = ProcessedContentView.Appearance(),
            contentProcessor: ContentProcessorProtocol = ContentProcessor()
        ) {
            self.appearance = appearance
            self.contentProcessor = contentProcessor
            self.htmlToAttributedStringConverter = HTMLToAttributedStringConverter(font: appearance.labelFont)
        }

        static func quizContent(
            textFont: UIFont = UIFont.preferredFont(forTextStyle: .body),
            textColor: UIColor = UIColor.primaryText,
            backgroundColor: UIColor = UIColor.systemBackground
        ) -> Configuration {
            Configuration(
                appearance: .init(
                    labelFont: textFont,
                    backgroundColor: backgroundColor
                ),
                contentProcessor: ContentProcessor(
                    injections: ContentProcessor.defaultInjections + [
                        FontInjection(font: textFont),
                        TextColorInjection(dynamicColor: textColor)
                    ]
                )
            )
        }

        static func stepText(
            textFont: UIFont = UIFont.preferredFont(forTextStyle: .body),
            textColor: UIColor = UIColor.primaryText,
            backgroundColor: UIColor = UIColor.clear
        ) -> Configuration {
            Configuration(
                appearance: .init(
                    labelFont: textFont,
                    labelTextColor: textColor,
                    backgroundColor: backgroundColor
                ),
                contentProcessor: ContentProcessor(
                    injections: ContentProcessor.defaultInjections + [
                        StepStylesInjection(),
                        FontInjection(font: textFont),
                        TextColorInjection(dynamicColor: textColor)
                    ]
                ),
                htmlToAttributedStringConverter: HTMLToAttributedStringConverter(font: textFont)
            )
        }
    }
}

// MARK: - Preview -

#Preview("Plain text") {
    LatexView(
        text: "Plain text https://google.com/"
    )
    .padding()
}

#Preview("Rich text") {
    LatexView(
        text: "Rich <b>text</b>!!!"
    )
    .padding()
}

#Preview("Step text") {
    LatexView(
        text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
""",
        configuration: .stepText()
    )
    .padding()
}
