import Combine
import Foundation
import shared

final class StepQuizCodeViewModel: ObservableObject {
    weak var delegate: StepQuizChildQuizDelegate?

    private let blockOptions: Block.Options
    private let dataset: Dataset
    private let reply: Reply?

    private let viewDataMapper: StepQuizCodeViewDataMapper

    @Published private(set) var viewData: StepQuizCodeViewData

    init(blockOptions: Block.Options, dataset: Dataset, reply: Reply?, viewDataMapper: StepQuizCodeViewDataMapper) {
        self.blockOptions = blockOptions
        self.dataset = dataset
        self.reply = reply

        self.viewDataMapper = viewDataMapper
        self.viewData = self.viewDataMapper.mapCodeDataToViewData(blockOptions: self.blockOptions, reply: self.reply)
    }
}
