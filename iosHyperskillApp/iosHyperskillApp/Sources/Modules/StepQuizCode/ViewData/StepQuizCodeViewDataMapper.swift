import Foundation
import shared

class StepQuizCodeViewDataMapper {
    private let formatter: Formatter
    private let resourceProvider: ResourceProvider

    private let stepQuizStatsTextMapper: StepQuizStatsTextMapper

    init(
        formatter: Formatter,
        resourceProvider: ResourceProvider,
        stepQuizStatsTextMapper: StepQuizStatsTextMapper
    ) {
        self.formatter = formatter
        self.resourceProvider = resourceProvider
        self.stepQuizStatsTextMapper = stepQuizStatsTextMapper
    }

    func mapCodeDataToViewData(step: Step, reply: Reply?) -> StepQuizCodeViewData {
        let blockOptions = step.block.options

        let languageStringValue = reply?.language ?? blockOptions.limits?.first?.key
        let language: CodeLanguage? = {
            if let languageStringValue = languageStringValue {
                return CodeLanguage(rawValue: languageStringValue)
            }
            return nil
        }()

        let codeTemplate: String? = {
            guard let languageStringValue = languageStringValue else {
                return nil
            }

            if let codeTemplate = blockOptions.codeTemplates?[languageStringValue] {
                return codeTemplate
            } else if let language = language {
                return CodeLanguageSamples.sample(for: language)
            }

            return nil
        }()

        let samples = mapSamples(blockOptions.samples)

        let languageLimits: Limit? = {
            guard let languageStringValue = languageStringValue else {
                return nil
            }

            return blockOptions.limits?[languageStringValue]
        }()
        let executionTimeLimit: String? = {
            guard let timeLimit = languageLimits?.time ?? blockOptions.executionTimeLimit?.int32Value else {
                return nil
            }

            return formatter.secondsCount(timeLimit)
        }()
        let executionMemoryLimit: String? = {
            guard let memoryLimit = languageLimits?.memory ?? blockOptions.executionMemoryLimit?.int32Value else {
                return nil
            }

            return resourceProvider.getString(
                stringResource: Strings.StepQuizCode.memoryLimitValueResource,
                args: KotlinArray(size: 1, init: { _ in NSNumber(value: memoryLimit) })
            )
        }()

        return StepQuizCodeViewData(
            language: language,
            languageStringValue: languageStringValue,
            code: reply?.code ?? codeTemplate,
            codeTemplate: codeTemplate,
            samples: samples,
            executionTimeLimit: executionTimeLimit,
            executionMemoryLimit: executionMemoryLimit,
            stepText: step.block.text
        )
    }

    // MARK: Private API

    private func mapSamples(_ samples: [[String]]?) -> [StepQuizCodeViewData.Sample] {
        guard let samples = samples else {
            return []
        }

        var result = [StepQuizCodeViewData.Sample]()
        var displayIndex = 1

        for sample in samples {
            guard let input = sample.first,
                  let output = sample.last else {
                continue
            }

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
                    inputValue: input.trimmed(),
                    outputTitle: outputTitle,
                    outputValue: output.trimmed()
                )
            )

            displayIndex += 1
        }

        return result
    }
}
