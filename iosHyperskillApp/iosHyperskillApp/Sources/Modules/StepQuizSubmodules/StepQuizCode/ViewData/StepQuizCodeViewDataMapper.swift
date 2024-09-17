import Foundation
import shared

class StepQuizCodeViewDataMapper {
    private let formatter: Formatter
    private let resourceProvider: ResourceProvider

    init(
        formatter: Formatter,
        resourceProvider: ResourceProvider
    ) {
        self.formatter = formatter
        self.resourceProvider = resourceProvider
    }

    func mapCodeDataToViewData(step: Step, reply: Reply?) -> StepQuizCodeViewData {
        let blockOptions = step.block.options

        let languageStringValue = reply?.language ?? blockOptions.limits?.first?.key
        let language: CodeLanguage? = {
            if let languageStringValue {
                return CodeLanguage(rawValue: languageStringValue)
            }
            return nil
        }()
        let languageHumanReadableName = step.displayLanguage ?? language?.humanReadableName

        let codeTemplate: String? = {
            guard let languageStringValue else {
                return nil
            }

            if let codeTemplate = blockOptions.codeTemplates?[languageStringValue] {
                return codeTemplate
            } else if let language {
                return CodeLanguageSamples.sample(for: language)
            }

            return nil
        }()

        let samples = mapSamples(blockOptions.samples)

        return StepQuizCodeViewData(
            language: language,
            languageStringValue: languageStringValue,
            languageHumanReadableName: languageHumanReadableName,
            code: reply?.code ?? codeTemplate,
            codeTemplate: codeTemplate,
            samples: samples,
            stepText: step.block.text
        )
    }

    // MARK: Private API

    private func mapSamples(_ samples: [Block.OptionsSample]?) -> [StepQuizCodeViewData.Sample] {
        guard let samples else {
            return []
        }

        var result = [StepQuizCodeViewData.Sample]()
        var displayIndex = 1

        for sample in samples {
            let inputTitle = resourceProvider.getString(
                stringResource: Strings.StepQuizCode.sampleInputTitleResource,
                args: KotlinArray(size: 1, init: { _ in NSNumber(value: displayIndex) })
            )
            let outputTitle = resourceProvider.getString(
                stringResource: Strings.StepQuizCode.sampleOutputTitleResource,
                args: KotlinArray(size: 1, init: { _ in NSNumber(value: displayIndex) })
            )

            result.append(
                .init(
                    inputTitle: inputTitle,
                    inputValue: sample.input.trimmed(),
                    outputTitle: outputTitle,
                    outputValue: sample.output.trimmed()
                )
            )

            displayIndex += 1
        }

        return result
    }
}
