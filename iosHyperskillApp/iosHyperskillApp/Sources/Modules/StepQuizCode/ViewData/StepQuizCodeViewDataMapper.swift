import Foundation
import shared

final class StepQuizCodeViewDataMapper {
    private let formatter: Formatter
    private let resourceProvider: ResourceProvider

    init(formatter: Formatter, resourceProvider: ResourceProvider) {
        self.formatter = formatter
        self.resourceProvider = resourceProvider
    }

    func mapCodeDataToViewData(blockOptions: Block.Options) -> StepQuizCodeViewData {
        let samples = mapSamples(blockOptions.samples)

        let executionTimeLimit: String? = {
            if let executionTimeLimit = blockOptions.executionTimeLimit {
                return formatter.secondsCount(executionTimeLimit.int32Value)
            }
            return nil
        }()
        let executionMemoryLimit: String? = {
            if let executionMemoryLimit = blockOptions.executionMemoryLimit {
                return resourceProvider.getString(
                    stringResource: Strings.StepQuizCode.memoryLimitValueResource,
                    args: KotlinArray(size: 1, init: { _ in NSNumber(value: executionMemoryLimit.intValue) })
                )
            }
            return nil
        }()

        return StepQuizCodeViewData(
            samples: samples,
            executionTimeLimit: executionTimeLimit,
            executionMemoryLimit: executionMemoryLimit
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
