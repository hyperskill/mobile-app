import Foundation
import shared

final class StepViewDataMapper {
    private let dateFormatter: SharedDateFormatter
    private let resourceProvider: ResourceProvider

    init(
        dateFormatter: SharedDateFormatter,
        resourceProvider: ResourceProvider
    ) {
        self.dateFormatter = dateFormatter
        self.resourceProvider = resourceProvider
    }

    func mapStepToViewData(_ step: Step) -> StepViewData {
        let formattedTimeToComplete = mapTimeToComplete(seconds: step.secondsToComplete?.floatValue ?? 60)

        return StepViewData(
            title: step.title,
            formattedTimeToComplete: formattedTimeToComplete,
            text: step.block.text
        )
    }

    // MARK: Private API

    private func mapTimeToComplete(seconds: Float) -> String {
        let minutesQuantityString = dateFormatter.formatMinutesOrSecondsCount(secondsToFormat: seconds)

        return resourceProvider.getString(
            stringResource: SharedResources.strings.shared.step_theory_reading_text,
            args: KotlinArray(size: 1, init: { _ in NSString(string: minutesQuantityString) })
        )
    }
}
