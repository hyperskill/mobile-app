import Foundation
import Highlightr
import shared

final class StepQuizFillBlanksViewDataMapper {
    private let fillBlanksItemMapper: FillBlanksItemMapper
    private let highlightr: Highlightr
    private let codeEditorThemeService: CodeEditorThemeServiceProtocol
    private let cache: StepQuizFillBlanksViewDataMapperCacheProtocol

    init(
        fillBlanksItemMapper: FillBlanksItemMapper,
        highlightr: Highlightr,
        codeEditorThemeService: CodeEditorThemeServiceProtocol,
        cache: StepQuizFillBlanksViewDataMapperCacheProtocol
    ) {
        let theme = codeEditorThemeService.theme
        highlightr.setTheme(to: theme.name)
        highlightr.theme.setCodeFont(theme.font)

        self.highlightr = highlightr
        self.codeEditorThemeService = codeEditorThemeService
        self.cache = cache
        self.fillBlanksItemMapper = fillBlanksItemMapper
    }

    func mapToViewData(dataset: Dataset, reply: Reply?) -> StepQuizFillBlanksViewData {
        guard let fillBlanksData = fillBlanksItemMapper.map(dataset: dataset, reply: reply) else {
            return .init(components: [], options: [])
        }

        return mapFillBlanksDataToViewData(fillBlanksData)
    }

    private func mapFillBlanksDataToViewData(_ fillBlanksData: FillBlanksData) -> StepQuizFillBlanksViewData {
        let language = fillBlanksData.language

        var components = fillBlanksData.fillBlanks
            .map { mapFillBlanksItem($0, language: language) }
            .flatMap { $0 }
        for index in components.indices {
            components[index].id = index
        }

        let options = fillBlanksData.options.map {
            StepQuizFillBlankOption(originalText: $0.originalText, displayText: $0.displayText)
        }

        return StepQuizFillBlanksViewData(components: components, options: options)
    }

    private func mapFillBlanksItem(
        _ fillBlanksItem: FillBlanksItem,
        language: String?
    ) -> [StepQuizFillBlankComponent] {
        switch FillBlanksItemKs(fillBlanksItem) {
        case .text(let data):
            var result = [StepQuizFillBlankComponent]()

            if data.startsWithNewLine {
                result.append(StepQuizFillBlankComponent(type: .lineBreak))
            }

            let hash = data.text.hashValue ^ UITraitCollection.current.userInterfaceStyle.hashValue

            if let cachedCode = cache.getHighlightedCode(for: hash) {
                result.append(StepQuizFillBlankComponent(type: .text, attributedText: cachedCode))
            } else {
                let unescaped = HTMLString.unescape(string: data.text)

                if let highlightedCode = highlight(code: unescaped, language: language) {
                    cache.setHighlightedCode(highlightedCode, for: hash)
                    result.append(StepQuizFillBlankComponent(type: .text, attributedText: highlightedCode))
                } else {
                    let attributedText = NSAttributedString(
                        string: unescaped,
                        attributes: [.font: codeEditorThemeService.theme.font]
                    )
                    cache.setHighlightedCode(attributedText, for: hash)
                    result.append(StepQuizFillBlankComponent(type: .text, attributedText: attributedText))
                }
            }

            return result
        case .input(let data):
            return [StepQuizFillBlankComponent(type: .input, inputText: data.inputText)]
        case .select(let data):
            return [StepQuizFillBlankComponent(type: .select, selectedOptionID: data.selectedOptionId?.intValue)]
        }
    }

    private func highlight(code: String, language: String?) -> NSAttributedString? {
        highlightr.highlight(code, as: language, fastRender: true)
    }
}
