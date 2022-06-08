import SwiftUI
import UIKit

struct SwiftUIProcessedContentView: UIViewRepresentable {
    typealias UIViewType = ProcessedContentView

    @Binding var text: String?

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
    }
}

// MARK: - SwiftUIProcessedContentView (Previews) -

// swiftlint:disable all
let latexViewText = """
<p style=\"text-align: center;\"><a href=\"http://atomickotlin.com\" rel=\"nofollow noopener noreferrer\"><img alt=\"\" src=\"https://ucarecdn.com/57ea9a4e-b8a9-4d14-9748-d1851cc58247/\" width=\"70\"></a></p>\n\n<p> </p>\n\n<h1 style=\"text-align: center;\">Manipulating Lists</h1>\n\n<blockquote>\n<p>Instead of implementing your own operations for manipulating lists, you can combine standard library operations.</p>\n</blockquote>\n\n<p>This atom shows operations that manipulate several lists by combining them in different ways.</p>
"""

struct LatexView_Previews: PreviewProvider {
    static var previews: some View {
        SwiftUIProcessedContentView(text: .constant(latexViewText))
            .frame(width: 200, height: 200, alignment: .center)
    }
}
