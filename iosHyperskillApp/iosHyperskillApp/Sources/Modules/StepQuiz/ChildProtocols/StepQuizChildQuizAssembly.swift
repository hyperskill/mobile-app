import Foundation
import shared
import SwiftUI

protocol StepQuizChildQuizAssembly: Assembly {
    var delegate: StepQuizChildQuizDelegate? { get set }

    init(blockOptions: Block.Options, dataset: Dataset, reply: Reply?, delegate: StepQuizChildQuizDelegate?)
}

// TODO: try Swift 5.7
// Causes error: Segmentation fault: 11
//enum StepQuizChildQuizAssemblyFactory {
//    static func make(
//        quizType: StepQuizChildQuizType,
//        dataset: Dataset,
//        reply: Reply?,
//        quizDelegate: StepQuizChildQuizDelegate?
//    ) -> some StepQuizChildQuizAssembly {
//        switch quizType {
//        case .choice:
//            return StepQuizChoiceAssembly(dataset: dataset, reply: reply, delegate: quizDelegate)
//        case .unsupported(let blockName):
//            fatalError("Unsupported quiz = \(blockName)")
//        }
//    }
//}
