import Foundation
import shared

final class StepQuizCodeViewDataMapper {
    private let resourceProvider: ResourceProvider

    init(resourceProvider: ResourceProvider) {
        self.resourceProvider = resourceProvider
    }

    func mapCodeDataToViewData(blockOptions: Block.Options) -> StepQuizCodeViewData {
        let samples = mapSamples(blockOptions.samples)

        return StepQuizCodeViewData(samples: samples)
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

            let inputTitle = mapSampleInputTitle(index: displayIndex)
            let outputTitle = mapSampleOutputTitle(index: displayIndex)

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

    private func mapSampleInputTitle(index: Int) -> String {
        resourceProvider.getString(
            stringResource: Strings.StepQuizCode.sampleInputTitleResource,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: index) })
        )
    }

    private func mapSampleOutputTitle(index: Int) -> String {
        resourceProvider.getString(
            stringResource: Strings.StepQuizCode.sampleOutputTitleResource,
            args: KotlinArray(size: 1, init: { _ in NSNumber(value: index) })
        )
    }
}
