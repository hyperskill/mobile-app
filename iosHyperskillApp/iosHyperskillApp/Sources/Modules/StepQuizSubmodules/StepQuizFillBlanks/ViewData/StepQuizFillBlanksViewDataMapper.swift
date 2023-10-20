import Foundation
import Highlightr
import shared

final class StepQuizFillBlanksViewDataMapper {
    private let highlightr: Highlightr
    private let codeEditorThemeService: CodeEditorThemeServiceProtocol
    private let cache: StepQuizFillBlanksViewDataMapperCacheProtocol

    init(
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
    }

    func mapToViewData(dataset: Dataset, reply: Reply?) -> StepQuizFillBlanksViewData {
        guard let fillBlanksData = FillBlanksItemMapper.shared.map(dataset: dataset, reply: reply) else {
            return .init(components: [])
        }

        return mapFillBlanksDataToViewData(fillBlanksData)
    }

    private func mapFillBlanksDataToViewData(_ fillBlanksData: FillBlanksData) -> StepQuizFillBlanksViewData {
        let language = fillBlanksData.language
        let components = fillBlanksData.fillBlanks.map { mapFillBlanksItem($0, language: language) }
        return StepQuizFillBlanksViewData(components: components)
    }

    private func mapFillBlanksItem(
        _ fillBlanksItem: FillBlanksItem,
        language: String?
    ) -> StepQuizFillBlankComponent {
        switch FillBlanksItemKs(fillBlanksItem) {
        case .text(let data):
            let hash = data.text.hashValue ^ UITraitCollection.current.userInterfaceStyle.hashValue

            if let cachedCode = cache.getHighlightedCode(for: hash) {
                return StepQuizFillBlankComponent(
                    id: data.id.intValue,
                    type: .text,
                    attributedText: cachedCode
                )
            } else {
                let unescaped = HTMLString.unescape(string: data.text)

                if let highlightedCode = highlight(code: unescaped, language: language) {
                    cache.setHighlightedCode(highlightedCode, for: hash)
                    return StepQuizFillBlankComponent(
                        id: data.id.intValue,
                        type: .text,
                        attributedText: highlightedCode
                    )
                } else {
                    let attributedText = NSAttributedString(
                        string: unescaped,
                        attributes: [
                            .font: codeEditorThemeService.theme.font
                        ]
                    )
                    cache.setHighlightedCode(attributedText, for: hash)
                    return StepQuizFillBlankComponent(
                        id: data.id.intValue,
                        type: .text,
                        attributedText: attributedText
                    )
                }
            }
        case .input(let data):
            return StepQuizFillBlankComponent(
                id: data.id.intValue,
                type: .input,
                inputText: data.inputText
            )
        }
    }

    private func highlight(code: String, language: String?) -> NSAttributedString? {
        highlightr.highlight(code, as: language, fastRender: true)
    }
}
