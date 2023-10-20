import Foundation
import shared

final class StepQuizFillBlanksViewDataMapper {
    func mapToViewData(dataset: Dataset, reply: Reply?) -> StepQuizFillBlanksViewData {
        guard let fillBlanksData = FillBlanksItemMapper.shared.map(dataset: dataset, reply: reply) else {
            return .init(components: [])
        }

        return mapFillBlanksDataToViewData(fillBlanksData)
    }

    private func mapFillBlanksDataToViewData(_ fillBlanksData: FillBlanksData) -> StepQuizFillBlanksViewData {
        let components = fillBlanksData.fillBlanks.map(mapFillBlanksItem(_:))
        return StepQuizFillBlanksViewData(components: components)
    }

    private func mapFillBlanksItem(_ fillBlanksItem: FillBlanksItem) -> StepQuizFillBlankComponent {
        switch FillBlanksItemKs(fillBlanksItem) {
        case .text(let data):
            return StepQuizFillBlankComponent(
                id: data.id.intValue,
                type: .text,
                text: data.text
            )
        case .input(let data):
            return StepQuizFillBlankComponent(
                id: data.id.intValue,
                type: .input,
                text: data.inputText
            )
        }
    }
}
