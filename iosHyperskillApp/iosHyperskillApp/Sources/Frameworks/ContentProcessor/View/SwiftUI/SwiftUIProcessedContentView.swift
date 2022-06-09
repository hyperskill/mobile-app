import SwiftUI
import UIKit

struct SwiftUIProcessedContentView: UIViewRepresentable {
    typealias UIViewType = ProcessedContentView

    @Binding var text: String

    var configuration = Configuration()

    var onContentLoaded: (() -> Void)?

    var onHeightUpdated: ((Int) -> Void)?

    var onOpenImageURL: ((URL) -> Void)?

    var onOpenLink: ((URL) -> Void)?

    // MARK: UIViewRepresentable

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
        processedContentView.setText(self.text)

        context.coordinator.onContentLoaded = self.onContentLoaded
        context.coordinator.onHeightUpdated = self.onHeightUpdated
        context.coordinator.onOpenImageURL = self.onOpenImageURL
        context.coordinator.onOpenLink = self.onOpenLink
    }
}

// MARK: - LatexView (Coordinator) -

extension SwiftUIProcessedContentView {
    class Coordinator: NSObject, ProcessedContentViewDelegate {
        var onContentLoaded: (() -> Void)?

        var onHeightUpdated: ((Int) -> Void)?

        var onOpenImageURL: ((URL) -> Void)?

        var onOpenLink: ((URL) -> Void)?

        func processedContentViewDidLoadContent(_ view: ProcessedContentView) {
            self.onContentLoaded?()
        }

        func processedContentView(_ view: ProcessedContentView, didReportNewHeight height: Int) {
            self.onHeightUpdated?(height)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenImageURL url: URL) {
            self.onOpenImageURL?(url)
        }

        func processedContentView(_ view: ProcessedContentView, didOpenLink url: URL) {
            self.onOpenLink?(url)
        }
    }
}

// MARK: - SwiftUIProcessedContentView (Configuration) -

extension SwiftUIProcessedContentView {
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
            textColor: UIColor = UIColor.primaryText
        ) -> Configuration {
            Configuration(
                appearance: .init(labelFont: textFont),
                contentProcessor: ContentProcessor(
                    injections: ContentProcessor.defaultInjections + [
                        FontInjection(font: textFont),
                        TextColorInjection(dynamicColor: textColor)
                    ]
                )
            )
        }
    }
}
