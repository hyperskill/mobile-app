import Foundation
import Highlightr
import shared

final class StepQuizParsonsViewDataMapper {
    private let highlightr: Highlightr?

    init(highlightr: Highlightr? = Highlightr()) {
        self.highlightr = highlightr
    }

    func mapToViewData(step: Step, dataset: Dataset, reply: Reply?) -> StepQuizParsonsViewData {
        guard let datasetLines = dataset.lines else {
            return StepQuizParsonsViewData(lines: [])
        }

        let languageStringValue = step.block.options.language
        let language: CodeLanguage? = {
            if let languageStringValue {
                return CodeLanguage(rawValue: languageStringValue)
            }
            return nil
        }()
        #if DEBUG
        if language == nil {
            print(
                """
StepQuizParsonsViewDataMapper :: unable to parse language from step.block.options.language = \
\(String(describing: languageStringValue))
"""
            )
        }
        #endif

        if let replyLines = reply?.lines {
            return StepQuizParsonsViewData(
                lines: replyLines.map { line in
                    let lineNumber = Int(line.lineNumber)
                    let rawText = datasetLines[lineNumber]

                    return .init(
                        lineNumber: lineNumber,
                        code: mapRawTextToCodeContent(rawText: rawText, language: language),
                        level: Int(line.level)
                    )
                }
            )
        } else {
            return StepQuizParsonsViewData(
                lines: datasetLines.enumerated().map {
                    .init(
                        lineNumber: $0,
                        code: mapRawTextToCodeContent(rawText: $1, language: language),
                        level: 0
                    )
                }
            )
        }
    }

    private func mapRawTextToCodeContent(
        rawText: String,
        language: CodeLanguage?
    ) -> StepQuizParsonsViewData.CodeContent {
        if let attributedString = highlight(code: rawText, language: language?.highlightr) {
            return .attributedString(attributedString)
        }
        return .htmlText("<code style=\"border: 0; padding: 0; margin: 0; background: transparent\">\(rawText)</code>")
    }

    private func highlight(code: String, language: String?) -> NSAttributedString? {
        guard let highlightr else {
            return nil
        }

        return highlightr.highlight(code, as: language, fastRender: true)
    }
}
