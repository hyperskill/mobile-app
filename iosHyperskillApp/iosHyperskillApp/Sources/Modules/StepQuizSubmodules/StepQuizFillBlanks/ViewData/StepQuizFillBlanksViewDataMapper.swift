import Foundation
import shared

final class StepQuizFillBlanksViewDataMapper {
    func mapToViewData(dataset: Dataset, reply: Reply?) -> StepQuizFillBlanksViewData {
        guard let fillBlanksData = FillBlanksItemMapper.shared.map(dataset: dataset, reply: reply) else {
            return .init(items: [])
        }

        return mapFillBlanksDataToViewData(fillBlanksData)
    }

    private func mapFillBlanksDataToViewData(_ fillBlanksData: FillBlanksData) -> StepQuizFillBlanksViewData {
        let items = fillBlanksData.fillBlanks.map(mapFillBlanksItem(_:))
        return StepQuizFillBlanksViewData(items: items)
    }

    private func mapFillBlanksItem(_ fillBlanksItem: FillBlanksItem) -> StepQuizFillBlankItem {
        switch FillBlanksItemKs(fillBlanksItem) {
        case .text(let data):
            return StepQuizFillBlankItem.text(data.text)
        case .input(let data):
            return StepQuizFillBlankItem.input(data.inputText)
        }
    }
}
