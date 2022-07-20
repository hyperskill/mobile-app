import SwiftUI

enum StepQuizSkeletonViewFactory {
    @ViewBuilder
    static func makeSkeleton(for quizType: StepQuizChildQuizType) -> some View {
        switch quizType {
        case .choice:
            StepQuizChoiceSkeletonView()
        case .matching:
            StepQuizMatchingSkeletonView()
        case .sorting:
            StepQuizSortingSkeletonView()
        case .table:
            StepQuizTableSkeletonView()
        case .string, .number, .math:
            StepQuizStringSkeletonView()
        case .unsupported(let blockName):
            fatalError("StepQuizSkeletonViewFactory :: did receive unsupported quizType = \(blockName)")
        }
    }
}
